<template>
  <el-header class="header">
    <!--    <nav>
          <RouterLink to="/">Home</RouterLink>
          <RouterLink to="/write">コンテンツ作成</RouterLink>
        </nav>-->
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/write">コンテンツ作成</el-menu-item>
    </el-menu>
  </el-header>
</template>
<script setup lang="ts">

</script>
<style scoped>
.header {
  padding: 0;
  height: 60px;
}
</style>