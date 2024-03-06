<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <nav>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/write">コンテンツ作成</RouterLink>
    </nav>
  </header>

  <RouterView />
</template>
<style>
</style>