<template>
  <a-layout id="components-layout">
    <div class="nav-right">
        <a-button class="signup-button" @click="moveToJoinForm"> 회원가입</a-button>
        <a-button class="login-button" @click="showLoginModal">로그인</a-button>
    </div>
    <a-layout-header id="header">
      <div class="header-container">
        <img src="@/assets/img/logo.png" alt="logo" width="100" height="100" @click="redirectToHome">
        <a class="logo-text" @click="redirectToHome">스탁메이트</a>
      </div>
      <div class="menu-container">
        <a-menu
            v-model:selectedKeys="menuKey"
            class="menu"
            mode="horizontal"
            :style="{
                float: 'right',
                border: 'none',
              }"
        >
          <a-menu-item key="news">
            <router-link to="/news">뉴스</router-link>
          </a-menu-item>
          <a-menu-item key="stockToday">
            <router-link to="/stock-today">오늘의 주식</router-link>
          </a-menu-item>
          <a-menu-item key="Chart">
            <router-link to="/chart">주식 차트</router-link>
          </a-menu-item>
          <a-menu-item key="MyChart">
            <router-link to="/mychart">나만의 차트</router-link>
          </a-menu-item>
          <a-menu-item key="MyPage">
            <router-link to="/mypage">마이페이지</router-link>
          </a-menu-item>
        </a-menu>
      </div>
    </a-layout-header>
  </a-layout>
</template>

<script setup>
// Vue Router의 useRoute나 useRouter를 사용해도 됩니다.

import router from "@/router";
import {ref} from "vue";


const menuKey = ref(['home']);
const redirectToHome = () => {
  router.push('/');
  menuKey.value = ['home']
};
</script>


<style lang="scss" scoped>
#components-layout {
  position: fixed;
  top: 0;
  left: 0;
  height: 10px;
  width: 100vw;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 1000;
}
#header {
  position: fixed;
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #ffffff;
  align-items: center;


  a {
    font-weight: 600;
    font-size: 50px;
    line-height: 48px;
    padding: 3px;
    color: #092d37;

  }

  .logo {
    display: flex;
    align-items: center;
    float: left;
    height: 64px;
    margin-top: 80px;
    margin-left: 100px;
  }

  .menu {
    margin-top: 80px;
    display: flex; /* 추가: 메뉴 아이템들이 한 줄에 표시되도록 함 */
  }
}

#components-layout .menu {
  a {
    font-weight: 600;
    font-size: 25px;
    color: black;
    padding: 0 50px; /* 추가: 메뉴 아이템 간격을 더 넓힘 */
  }
}

.nav-right {
  float: right;
  padding-right: 20px;
  padding-top: 20px;
  margin-left: auto;
  button {
    border-radius: 10px;
  }
  z-index: 100000;
}
#components-layout .col_left {
  display: flex;
  align-items: center;
  float: left;
  height: 64px;
  margin-top: 50px;
  margin-left: 100px;
}

.header-container {
  display: flex;
  align-items: center;
  float: left;
  height: 64px;
  margin-top: 50px;
  margin-left: 100px;
}

.menu-container {
  height: 70%;
  display: flex;
  align-items: center;
  float: right;
  margin-top: 50px;
  margin-left: 100px;
  margin-right: 10%;
}

.ant-btn {
  margin-left: 10px;
}

</style>