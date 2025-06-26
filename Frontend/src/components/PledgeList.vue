<template>
  <div class="card">
    <div class="card-body">
      <h5 class="card-title">{{ title }}</h5>
      
      <div v-if="loading" class="text-center py-3">
        <div class="spinner-border spinner-border-sm" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>

      <div v-else-if="pledges.length">
        <div v-for="pledge in pledges" :key="pledge.id" class="mb-3">
          <div class="d-flex justify-content-between align-items-start">
            <div>
              <div class="d-flex gap-2 align-items-center">
                <span class="fw-medium">{{ pledge.supporter.username }}</span>
                <span class="badge bg-success">${{ pledge.amount }}</span>
              </div>
              <small v-if="pledge.comment" class="text-muted d-block">
                {{ pledge.comment }}
              </small>
            </div>
            <small class="text-muted">{{ formatDate(pledge.createdAt) }}</small>
          </div>
          
          <div v-if="showProject" class="mt-2">
            <RouterLink 
              :to="{ name: 'project-details', params: { id: pledge.project.id }}"
              class="small text-decoration-none"
            >
              {{ pledge.project.title }}
            </RouterLink>
            <ProgressBar 
              :value="calculateProgress(pledge.project)" 
              :height="4" 
              :show-label="false" 
              class="mt-1" 
            />
          </div>
        </div>
      </div>
      
      <p v-else class="text-muted mb-0 text-center">
        {{ emptyMessage }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import ProgressBar from './ProgressBar.vue';

const props = defineProps({
  pledges: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  showProject: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: 'Recent Pledges'
  },
  emptyMessage: {
    type: String,
    default: 'No pledges yet'
  }
});

const calculateProgress = (project) => {
  if (!project?.fundingGoal) return 0;
  return Math.round((project.currentFunding / project.fundingGoal) * 100);
};

const formatDate = (date) => {
  return new Date(date).toLocaleDateString();
};
</script> 