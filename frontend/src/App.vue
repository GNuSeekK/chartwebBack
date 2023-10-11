<script setup>
import LoginModal from "@/components/Modal/LoginModal.vue";
import {inject, provide, reactive, ref} from "vue";

import {useCookies} from "vue3-cookies";
import axios from "axios";
axios.defaults.withCredentials = true;
axios.defaults.baseURL = 'http://localhost:8080';

provide('axios', axios)
const {cookies} = useCookies();
provide('cookies', cookies)

const isLoginModal = ref(false);
const loginModalChange = () => {
  isLoginModal.value = !isLoginModal.value;
  console.log(isLoginModal.value);
}
provide('loginModalChange', loginModalChange);
provide('isLoginModal', isLoginModal);
const accessToken = ref('');
provide('accessToken', accessToken);

const isLogin = ref(false);
provide('isLogin', isLogin);
const userInfo = reactive({
  id: '',
  email: '',
  nickname: '',
})
provide('userInfo', userInfo);
const logout = () => {
  accessToken.value = '';
  userInfo.id = '';
  userInfo.email = '';
  userInfo.nickname = '';
  isLogin.value = false;
  cookies.remove("refreshToken");
}
provide('logout', logout);
const test = () => {
axios.post('http://localhost:8080/login/test', {}, {
    headers: {
      Authorization: `Bearer ${accessToken.value}`
    }
  })
  .then(res => {
    console.log(res);
  })
}
const reissueToken = () => {
  axios.get('http://localhost:8080/login/token/reissue', {
    headers: {
      withCredentials: true,
    }
  })
  .then(res => {
    accessToken.value = res.data.accessToken;
    console.log(accessToken.value);
  })

  getMemberInfo()
}
provide('reissueToken', reissueToken);

const getMemberInfo = () => {
  console.log(accessToken.value)
  axios.get('http://localhost:8080/member/info', {
    headers: {
      Authorization: `Bearer ${accessToken.value}`
    }
  })
  .then(res => {
    userInfo.id = res.data.id;
    userInfo.email = res.data.email;
    userInfo.nickname = res.data.nickname;
    console.log(userInfo);
  }).catch(err => {
    console.log(err);
  })
}
provide('getMemberInfo', getMemberInfo);

</script>

<template>
  <!--  <div-button :text="'로그인'" @click="openLoginModal">-->
  <!--    <template>-->
  <!--      <h1>1234</h1>-->
  <!--    </template>-->
  <!--  </div-button>-->
  <button @click="loginModalChange" v-if="!isLoginModal.valueOf()">로그인</button>
  <button @click="logout" v-else>로그아웃</button>
  <button @click="test">테스트</button>
  <button @click="reissueToken">재발급</button>
  <h2>{{userInfo.email}}</h2>
  <LoginModal v-if="isLoginModal.valueOf()"/>

</template>

<style scoped>
</style>
