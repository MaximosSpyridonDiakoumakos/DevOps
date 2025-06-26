<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { useApplicationStore } from '@/stores/application.js';

const route = useRoute();
const { isAuthenticated, userData } = useApplicationStore();

const isCreator = computed(() => userData.value?.role === 'ROLE_CREATOR');
const isSupporter = computed(() => userData.value?.role === 'ROLE_SUPPORTER');
const isAdmin = computed(() => userData.value?.role === 'ROLE_ADMIN');
const isAuthPage = computed(() => ['/login', '/signup'].includes(route.path));
</script>

<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light mb-4">
    <div class="container">
      <RouterLink class="navbar-brand" to="/">Crowdfunding Platform</RouterLink>
      
      <button 
        class="navbar-toggler" 
        type="button" 
        data-bs-toggle="collapse" 
        data-bs-target="#navbarNav"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <RouterLink class="nav-link" to="/projects">Browse Projects</RouterLink>
          </li>
          <li v-if="isCreator" class="nav-item">
            <RouterLink class="nav-link" to="/creator/dashboard">My Projects</RouterLink>
          </li>
          <li v-if="isSupporter" class="nav-item">
            <RouterLink class="nav-link" to="/supporter/dashboard">My Pledges</RouterLink>
          </li>
          <li v-if="isAdmin" class="nav-item">
            <RouterLink class="nav-link" to="/admin">Admin Dashboard</RouterLink>
          </li>
        </ul>
        
        <ul class="navbar-nav">
          <template v-if="!isAuthenticated">
            <li v-if="!isAuthPage" class="nav-item">
              <RouterLink class="nav-link" to="/signup">Register</RouterLink>
            </li>
            <li v-if="!isAuthPage" class="nav-item">
              <RouterLink class="nav-link" to="/login">Login</RouterLink>
            </li>
          </template>
          <template v-else-if="!isAuthPage">
            <li class="nav-item">
              <RouterLink class="nav-link" to="/profile">My Profile</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-link" to="/logout">Logout</RouterLink>
            </li>
          </template>
        </ul>
      </div>
    </div>
  </nav>
</template>
