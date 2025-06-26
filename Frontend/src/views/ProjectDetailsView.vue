<template>
  <div class="bg-body-tertiary">
    <div class="container">
      <div class="row py-4 px-3">
        <div class="col-12">
          <div class="mb-4">
            <RouterLink class="small" :to="{ name: 'projects' }">Back to Projects</RouterLink>
            <h1 class="fs-3">{{ project.title }}</h1>
          </div>
          
          <div class="row">
            <div class="col-md-8">
              <p class="lead">{{ project.description }}</p>
              
              <div class="card mb-4">
                <div class="card-body">
                  <h5 class="card-title">Funding Progress</h5>
                  <ProgressBar :value="fundingProgress" class="mb-3" />
                  
                  <div class="d-flex justify-content-between">
                    <div>
                      <h6>Current Funding</h6>
                      <p class="h4">${{ project.currentFunding }}</p>
                    </div>
                    <div>
                      <h6>Goal</h6>
                      <p class="h4">${{ project.fundingGoal }}</p>
                    </div>
                    <div>
                      <h6>Time Left</h6>
                      <p class="h4">{{ calculateDaysLeft() }} days</p>
                    </div>
                  </div>
                </div>
              </div>

              <div class="creator-info card mb-4">
                <div class="card-body">
                  <h5 class="card-title">About the Creator</h5>
                  <p>{{ project.creator?.bio || 'No bio provided.' }}</p>
                </div>
              </div>

              <ProjectUpdates 
                :project-id="project.id"
                :is-creator="isProjectCreator"
              />
            </div>
            
            <div class="col-md-4">
              <PledgeForm
                v-if="canPledge"
                :project-id="project.id"
                :funding-goal="project.fundingGoal"
                :current-funding="project.currentFunding"
                @pledge-submitted="refreshProject"
              />
              
              <div v-else-if="!isAuthenticated" class="card mb-4">
                <div class="card-body text-center">
                  <p>Please sign in as a supporter to make a pledge</p>
                  <RouterLink :to="{ name: 'login' }" class="btn btn-primary">
                    Sign In
                  </RouterLink>
                </div>
              </div>

              <PledgeList
                :pledges="project.pledges || []"
                :loading="loading"
                title="Recent Pledges"
                class="mt-4"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useApplicationStore } from '@/stores/application.js';
import { useRemoteData } from '@/composables/useRemoteData.js';
import PledgeForm from '@/components/PledgeForm.vue';
import ProjectUpdates from '@/components/ProjectUpdates.vue';
import PledgeList from '@/components/PledgeList.vue';
import ProgressBar from '@/components/ProgressBar.vue';

const route = useRoute();
const { userData, isAuthenticated } = useApplicationStore();
const project = ref({});

const { data, performRequest } = useRemoteData(`/api/projects/${route.params.id}`, true);

const isProjectCreator = computed(() => 
  userData.value?.id === project.value?.creator?.id
);

const canPledge = computed(() => 
  isAuthenticated.value && 
  !isProjectCreator.value && 
  project.value?.status === 'ACTIVE'
);

const fundingProgress = computed(() => {
  if (!project.value?.fundingGoal) return 0;
  return Math.round((project.value.currentFunding / project.value.fundingGoal) * 100);
});

const calculateDaysLeft = () => {
  if (!project.value?.deadline) return 0;
  const deadline = new Date(project.value.deadline);
  const today = new Date();
  const diffTime = deadline - today;
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
};

const refreshProject = async () => {
  await performRequest();
  project.value = data.value;
};

onMounted(() => {
  refreshProject();
});
</script> 