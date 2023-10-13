<script setup>
import LoginModal from "@/components/Modal/LoginModal.vue";
import {provide, reactive, ref} from "vue";

import {useCookies} from "vue3-cookies";
import axios from "axios";
import ContentBox from "@/components/UI/ContentBox.vue";
import InputBox from "@/components/UI/InputBox.vue";
import BoxInnerContent from "@/components/UI/BoxInnerContent.vue";

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
  <!--  <div class="main">-->
  <!--    <MainSidebar>-->
  <!--    </MainSidebar>-->
  <!--    <div class="main"></div>-->
  <!--  </div>-->
  <ContentBox id="test">
    <h-title>My Chart</h-title>
    <div></div>
    <!--    <mini-round-div><a href="">My Chart</a></mini-round-div>-->
    <box-inner-content>
      <template v-slot:input>
        <input-box>
          <template v-slot:icon>
            <div>ID</div>
          </template>
          <template v-slot:default>
            <input type="text" placeholder="아이디">
          </template>
        </input-box>
        <input-box>
          <template v-slot:icon>
            <div>PW</div>
          </template>
          <template v-slot:default>
            <input type="text" placeholder="비밀번호">
          </template>
        </input-box>
      </template>
      <template v-slot:default>
        <div class="button-container">
          <button @click="loginModalChange">로그인</button>
          <button @click="test">테스트</button>
          <button @click="reissueToken">토큰 재발급</button>
        </div>
      </template>
    </box-inner-content>
  </ContentBox>
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
  display: block;
  padding: 0.5rem;
  margin: 1rem;
  background-color: #9ec2e7;
  color: #fff;
  font-weight: 600;
  text-align: center;
  border-radius: 8px;
  width: 100%;
  height: 40px;
  box-shadow: 0 1px 2px 1px rgb(114 125 137);
}


#test {
  width: 500px;
  height: 300px;
}

.button-container {
  display: flex;
}
</style>

<style>


h-title {
  font-size: 2rem;
  font-weight: 600;
  margin-bottom: 1rem;
  color: #da6596;
}

body, input, textarea, select, button, table {
  font-size: 1.4rem;
  line-height: 1.7rem;
  color: #101010;
  font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕", helvetica, "Apple SD Gothic Neo", sans-serif;
  font-weight: 500;
  letter-spacing: -0.4px;
}

button, input, select, textarea {
  border-radius: 0;
  border: none;
  background: 0 0;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  outline: 0;
  text-decoration: none;
  cursor: pointer;
  -webkit-text-size-adjust: none;
}

body, p, h1, h2, h3, h4, h5, h6, ul, ol, li, dl, dt, dd, table, th, td, form, fieldset, legend, input, textarea, button, select {
  margin: 0;
  padding: 0;
}

span, h1, h2, h3, h4, h5, h6, h-title, p {
  cursor: default;
}

p {
  display: block;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0;
  margin-inline-end: 0;
}


a:-webkit-any-link {
  color: inherit;
  cursor: pointer;
  text-decoration: none;
}

a:visited {
  text-decoration: none;
  color: inherit;
}

a {
  text-decoration: none;
  color: inherit;
}

.bold {
  font-weight: bold;
}
</style>