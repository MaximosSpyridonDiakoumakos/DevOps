<template>
  <div class="bg-body-tertiary">
    <div class="container">
      <div class="row py-4 px-3">
        <div class="col-12">
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="fs-3">Browse Projects</h1>
            <RouterLink 
              v-if="isCreator" 
              :to="{ name: 'create-project' }" 
              class="btn btn-primary"
            >
              Create New Project
            </RouterLink>
          </div>
          
          <div v-if="loading" class="text-center py-4">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
          </div>

          <div v-else class="row">
            <div 
              v-for="project in projects" 
              :key="project.id" 
              class="col-md-4 mb-4"
            >
              <ProjectCard :project="project" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useApplicationStore } from '@/stores/application.js';
import { useRemoteData } from '@/composables/useRemoteData.js';
import ProjectCard from '@/components/ProjectCard.vue';

const { userData } = useApplicationStore();
const isCreator = computed(() => userData.value?.role === 'CREATOR');

const projects = ref([]);
const loading = ref(true);

const { data, performRequest } = useRemoteData('/api/projects', true);

onMounted(async () => {
  try {
    await performRequest();
    projects.value = data.value;
  } finally {
    loading.value = false;
  }
});
</script> 