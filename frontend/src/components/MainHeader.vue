<script>
import PLinkButton from "@/components/UI/PLinkButton.vue";
export default {
  name: 'MainHeader',
  components: {PLinkButton},
  data() {
    return {
      user: {
        logined: true,
        userId: '유저',
        userNm: '유저',
        userPw: '1234'
      }
    }
  },
  methods: {
    loginUser() {
      this.user.logined = true;
      this.user.userId = '유저';
      this.user.userNm = '유저';

    },
    logoutUser() {
      this.user.logined = false;
      this.user.userId = '';
      this.user.userNm = '';
    }
  }
}
</script>

<template>
  <header class="navbar navbar-expand-lg bg-body-tertiary shadow sbg-main-color" id="nav-header">
    <div class="container-fluid">
      <div class="div-flex">
<!--        <HomeButton/>-->
<!--        <NoticeInfo :user="user" v-if="user.logined"/>-->
      </div>
      <div class="collapse navbar-collapse justify-content-end" id="navbarNavAltMarkup1">
        <div class="navbar-nav" v-if="user.logined">
          <p-link-button :text="this.user.userNm"/>
          <p-link-button :text="'마이페이지'" data-bs-toggle="modal" data-bs-target="#my-page"/>
          <p-link-button @click="logoutUser" :text="'로그아웃'"/>
        </div>
        <div class="navbar-nav" v-else>
          <p-link-button :text="'회원가입'" data-bs-toggle="modal" data-bs-target="#sign-up-page"/>
          <p-link-button :text="'로그인'" data-bs-toggle="modal" data-bs-target="#login-page" @click="loginUser"/>

        </div>
      </div>
    </div>

  </header>
  <LoginModal id="login-page"/>
<!--  <SignUpModal id="sign-up-page"/>-->
<!--  <MyPageModal id="my-page" :user="user"/>-->

</template>

<style scoped>


</style>