<template>
  <div class="card mb-4">
    <div class="card-body">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="card-title mb-0">Project Updates</h5>
        <button 
          v-if="isCreator" 
          class="btn btn-outline-primary btn-sm"
          @click="showUpdateForm = true"
        >
          Post Update
        </button>
      </div>

      <div v-if="loading" class="text-center py-3">
        <div class="spinner-border spinner-border-sm" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>

      <div v-else>
        <div v-if="showUpdateForm && isCreator" class="mb-4">
          <form @submit.prevent="handleSubmit">
            <div class="mb-3">
              <label for="updateContent" class="form-label">Update Message</label>
              <textarea
                id="updateContent"
                v-model="newUpdate"
                class="form-control"
                :class="{ 'is-invalid': errors.content }"
                rows="3"
                required
              ></textarea>
              <div class="invalid-feedback">{{ errors.content }}</div>
            </div>
            <div class="d-flex justify-content-end gap-2">
              <button 
                type="button" 
                class="btn btn-outline-secondary"
                @click="cancelUpdate"
              >
                Cancel
              </button>
              <button 
                type="submit" 
                class="btn btn-primary"
                :disabled="loading"
              >
                {{ loading ? 'Posting...' : 'Post Update' }}
              </button>
            </div>
          </form>
        </div>

        <div v-if="updates.length">
          <div v-for="update in updates" :key="update.id" class="mb-4">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <h6 class="mb-0">{{ update.creator.username }}</h6>
              <small class="text-muted">{{ formatDate(update.createdAt) }}</small>
            </div>
            <p class="mb-0">{{ update.content }}</p>
          </div>
        </div>
        
        <div v-else class="text-center py-3">
          <p class="text-muted mb-0">No updates yet</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useApplicationStore } from '@/stores/application.js';
import { useRemoteData } from '@/composables/useRemoteData.js';

const props = defineProps({
  projectId: {
    type: String,
    required: true
  }
});

const { userData } = useApplicationStore();
const isCreator = computed(() => userData.value?.role === 'CREATOR');

const updates = ref([]);
const loading = ref(false);
const showUpdateForm = ref(false);
const newUpdate = ref('');
const errors = ref({});

const { data: updatesData, performRequest: fetchUpdates } = useRemoteData(
  `/api/projects/${props.projectId}/updates`,
  true
);

const { performRequest: postUpdate } = useRemoteData(
  `/api/projects/${props.projectId}/updates`,
  true,
  'POST'
);

const formatDate = (date) => {
  return new Date(date).toLocaleDateString();
};

const validateForm = () => {
  errors.value = {};
  if (!newUpdate.value.trim()) {
    errors.value.content = 'Update content is required';
    return false;
  }
  return true;
};

const cancelUpdate = () => {
  showUpdateForm.value = false;
  newUpdate.value = '';
  errors.value = {};
};

const handleSubmit = async () => {
  if (!validateForm()) return;
  
  loading.value = true;
  try {
    await postUpdate({ content: newUpdate.value.trim() });
    await loadUpdates();
    cancelUpdate();
  } catch (error) {
    console.error('Failed to post update:', error);
    alert('Failed to post update. Please try again.');
  } finally {
    loading.value = false;
  }
};

const loadUpdates = async () => {
  loading.value = true;
  try {
    await fetchUpdates();
    updates.value = updatesData.value;
  } finally {
    loading.value = false;
  }
};

loadUpdates();
</script> 