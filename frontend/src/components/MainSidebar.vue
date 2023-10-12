<script setup>
import {inject} from "vue";
import SideBarButton from "@/components/UI/SideBarButton.vue";

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
</script>

<template>

  <div class="menu">
    <side-bar-button>
      <template v-slot:default>
        <img src="../img/sidebar/home.svg" alt="">
      </template>
      <template v-slot:text v-if="userInfo.nickname!==''">
        {{userInfo.nickname}}
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
    <side-bar-button @click="logout" v-else>
      <template v-slot:default>
        <img src="../img/sidebar/account.svg" alt="">
      </template>
      <template v-slot:text>
        내정보
      </template>
    </side-bar-button>
  </div>
</template>

<style scoped>

.menu {
  flex: 200px 1;
  flex-basis: 30%;
  height: 100vh;
  max-width: 300px;
  min-width: 250px;
  background-color: #2090a9;
  justify-content: left;
}

</style>