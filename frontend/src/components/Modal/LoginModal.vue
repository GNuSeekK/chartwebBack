<template>
  <CommonModal>
    <div class="login">
      <div class="content">
        <form id="login-in">
          <h1 class="login__title">Stock Quant</h1>
          <div class="loginBox">
            <input type="text" placeholder="아이디" name="userId" v-model="loginUser.memberEmail"/>
          </div>

          <div class="loginBox">
            <input type="password" placeholder="비밀번호" name="userPw" v-model="loginUser.password"/>
          </div>

          <button type="button" @click="login">로그인</button>
          <button type="button" @click="login">취 소</button>
        </form>
      </div>
    </div>

  </CommonModal>
</template>

<script setup>
import CommonModal from "@/components/Modal/CommonModal.vue";
import {inject, reactive} from "vue";
const axios = inject('axios');
const getMemberInfo = inject('getMemberInfo');
const accessToken = inject('accessToken');
const loginUser = reactive({
  memberEmail: '',
  password: ''
})

const userInfo = inject('userInfo');
let loginModalChange = inject('loginModalChange');
const login = () => {
  axios.post('http://localhost:8080/login/member', {
    memberEmail: loginUser.memberEmail,
    password: loginUser.password
  })
  .then(res => {
    accessToken.value = res.data.accessToken;
    getMemberInfo();
    loginModalChange();
  }).catch(err => {
    console.log(err);
  })

}

</script>

<style scoped>
button {
  display: inline;
  padding: 1rem;
  margin: 1rem;
  background-color: #3e5eab;
  color: #fff;
  font-weight: 600;
  text-align: center;
  border-radius: 0.5rem;
  transition: 0.3s;
}
button:hover {
  background-color: #2090a9;
}

h1 {
  color: rgba(222, 16, 16, 0.82)
}

input {
  border: 1px black;
  font-size: 0.938rem;
  font-weight: 700;
  color: rgba(77, 32, 0, 0.82);
  width: 75%;
}

input::placeholder {
  font-size: 0.938rem;
  font-family: "Open Sans", sans-serif;
  color: #a49eac;
}

input:focus {
  border: 1px solid #027979;
}

.loginBox {
  grid-template-columns: max-content 1fr;
  column-gap: 0.5rem;
  padding: 1.125rem 1rem;
  background-color: #fff;
  margin-top: 1rem;
  border-radius: 0.5rem;
}

form {
  bottom: 1rem;
  width: 100%;
  height: 22rem;
  background-color: #ffffff;
  padding: 2rem 1rem;
  border-radius: 1rem;
  text-align: center;
  box-shadow: 0 8px 20px rgba(35, 0, 77, 0.2);
}


.login {
  grid-template-columns: 100%;
  height: 400px;
  width: 330px;
  margin-left: 1.5rem;
  margin-right: 1.5rem;
}
</style>