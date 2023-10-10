<script>
import LoginModal from "@/components/Modal/LoginModal.vue";
import axios from "axios";

export default {
  components: {
    LoginModal
  },
  data() {
    return {
      isLoginModal: false,
      accessToken: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzc2FmeSIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjk2OTQyMzg2fQ.WwtkL39J6rUiAu64C1HmKURW-WB7cCOhssGC2vh-eVI',
    }
  },
  methods: {
    openLoginModal() {
      this.isLoginModal = !this.isLoginModal;
    },
    logout() {
      this.accessToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzc2FmeSIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjk2OTQyMzg2fQ.WwtkL39J6rUiAu64C1HmKURW-WB7cCOhssGC2vh-eVI';
      this.$cookies.remove("refreshToken");
      this.isLogin = false;
      this.isLoginModal = false;
    },
    test() {
      axios.post('http://localhost:8080/login/test', {}, {
        headers: {
          Authorization: `Bearer ${this.accessToken}`
        }
      })
      .then(res => {
        console.log(res);
      })
    },
    reissue() {
      axios.get('http://localhost:8080/login/token/reissue', {
        headers: {
          withCredentials: true,
        }
      })
      .then(res => {
        console.log(res);
        this.$cookies.set("refreshToken", res.data.refreshToken, "14d", "/", "localhost", true, "None");
        console.log(this.$cookies.get("refreshToken"));
        this.accessToken = res.data.accessToken;
        console.log(this.accessToken);
      })
    }
  }
}
</script>

<template>
  <!--  <div-button :text="'로그인'" @click="openLoginModal">-->
  <!--    <template>-->
  <!--      <h1>1234</h1>-->
  <!--    </template>-->
  <!--  </div-button>-->
  <button @click="openLoginModal" v-if="!this.isLoginModal">로그인</button>
  <button @click="logout" v-else>로그아웃</button>
  <button @click="test">테스트</button>
  <button @click="reissue">재발급</button>
  <LoginModal v-if="isLoginModal"/>

</template>

<style scoped>
</style>
