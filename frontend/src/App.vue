<script setup>
import LoginModal from "@/components/Modal/LoginModal.vue";
import {provide, reactive, ref} from "vue";

import {useCookies} from "vue3-cookies";
import axios from "axios";
import MainSidebar from "@/components/MainSidebar.vue";

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
  <div class="main">
    <MainSidebar>
    </MainSidebar>
    <div class="main"></div>
  </div>
  <LoginModal v-if="isLoginModal.valueOf()"></LoginModal>
</template>

<style scoped>
.main {
  display: flex;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  background-color: #b0eefd;
  //margin-left: -12px;
}

.main {
  flex: 500px 1;
  flex-basis: 70%;
}



button {
//display: block; padding: 1rem;
  margin: 1rem;
  background-color: #3e5eab;
  color: #fff;
  font-weight: 600;
  text-align: center;
  border-radius: 12px;
}
</style>
