<script setup>
import {inject, ref} from "vue";
import SideBarButton from "@/components/UI/SideBarButton.vue";
import SideBarSubButton from "@/components/UI/SideBarSubButton.vue";

const userInfo = inject('userInfo');
const loginModalChange = inject('loginModalChange');
const isLogin = inject('isLogin');
const cookies = inject('cookies');
const accessToken = inject('accessToken');

const logout = () => {
  accessToken.value = '';
  userInfo.id = '';
  userInfo.email = '';
  userInfo.nickname = '';
  isLogin.value = false;
  cookies.remove("refreshToken");
}

const goHome = () => {
  location.href = '/';
}

const myInfoMenu = ref(false);
const myInfoMenuChange = () => {
  myInfoMenu.value = !myInfoMenu.value;
}

const searchMenu = ref(false);
const searchMenuChange = () => {
  searchMenu.value = !searchMenu.value;
}
</script>

<template>

  <div class="menu">
    <side-bar-button>
      <template v-slot:default>
        <img src="../img/sidebar/home.svg">
      </template>
      <template v-slot:text v-if="userInfo.nickname!==''">
        {{ userInfo.nickname }}
      </template>
      <template v-slot:text v-else @click="goHome">
        홈으로
      </template>
    </side-bar-button>
    <side-bar-button @click="loginModalChange" v-if="userInfo.id === ''">
      <template v-slot:default>
        <img src="../img/sidebar/account.svg" alt="">
      </template>
      <template v-slot:text>
        로그인
      </template>
    </side-bar-button>
    <side-bar-button v-else @mouseenter="myInfoMenuChange" @mouseleave="myInfoMenuChange">
      <template v-slot:default>
        <img src="../img/sidebar/account.svg" alt="">
      </template>
      <template v-slot:text>
        내정보
      </template>
      <template v-slot:sub v-if="myInfoMenu.valueOf()">
        <side-bar-sub-button>마이 페이지</side-bar-sub-button>
        <side-bar-sub-button>나의 관심그룹</side-bar-sub-button>
        <side-bar-sub-button>회원정보 수정</side-bar-sub-button>
        <side-bar-sub-button>비밀번호 변경</side-bar-sub-button>
        <side-bar-sub-button @click="logout">로그아웃</side-bar-sub-button>
      </template>
    </side-bar-button>
    <side-bar-button>
      <template v-slot:default>
        <img src="../img/sidebar/account.svg" alt="">
      </template>
      <template v-slot:text>
        오늘의 주식
      </template>
    </side-bar-button>
    <side-bar-button @mouseenter="searchMenuChange" @mouseleave="searchMenuChange">
      <template v-slot:default>
        <img src="../img/sidebar/account.svg" alt="">
      </template>
      <template v-slot:text>
        주가 검색창
      </template>
      <template v-slot:sub v-if="searchMenu.valueOf()">
        <side-bar-sub-button>종목 게시판</side-bar-sub-button>
        <side-bar-sub-button>종목 토론방</side-bar-sub-button>
        <side-bar-sub-button>관련 리포트</side-bar-sub-button>
      </template>
    </side-bar-button>
    <side-bar-button>
      <template v-slot:default>
        <img src="../img/sidebar/account.svg" alt="">
      </template>
      <template v-slot:text>
        주가 지수
      </template>
    </side-bar-button>
    <div class="img-wrapper">
      <img src="../img/stock_logo.jpg" alt="" class="logo">
    </div>

  </div>
</template>

<style scoped>

.menu {
  flex: 200px 1;
  flex-basis: 30%;
  height: 100vh;
  max-width: 300px;
  min-width: 250px;
//background-color: #2090a9; background-color: #0a2767;
  justify-content: left;
}

.img-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-self: flex-end;
}

img {
  width: 90%;
  border-radius: 50px;
//align-self: flex-end; //margin-bottom: 50%; box-shadow: 1px 1px 3px 3px #126e81;
}

img.logo {
  position: fixed;
//top: 70vh; bottom: 10vh;
  left: 20px;
  width: 210px;
  height: 180px;
  box-shadow: 3px 3px 3px 3px #126e81;
}
</style>