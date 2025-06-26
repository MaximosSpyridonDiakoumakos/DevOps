<template>
  <div class="card mb-4">
    <div class="card-body">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="card-title mb-0">Project Updates</h5>
        <button 
          v-if="isCreator" 
          class="btn btn-primary btn-sm"
          @click="showUpdateForm = true"
        >
          Add Update
        </button>
      </div>

      <form v-if="showUpdateForm" @submit.prevent="submitUpdate" class="mb-4">
        <div class="mb-3">
          <label for="updateContent" class="form-label">Update Content</label>
          <textarea
            id="updateContent"
            v-model="newUpdate"
            class="form-control"
            rows="3"
            required
          ></textarea>
        </div>
        <div class="d-flex gap-2">
          <button type="submit" class="btn btn-primary" :disabled="loading">
            Post Update
          </button>
          <button 
            type="button" 
            class="btn btn-secondary"
            @click="showUpdateForm = false"
          >
            Cancel
          </button>
        </div>
      </form>

      <div v-if="updates.length" class="updates-list">
        <div v-for="update in updates" :key="update.id" class="mb-3">
          <div class="d-flex justify-content-between">
            <small class="text-muted">{{ formatDate(update.createdAt) }}</small>
          </div>
          <p class="mb-0">{{ update.content }}</p>
        </div>
      </div>
      <p v-else class="text-center text-muted">No updates yet</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRemoteData } from '@/composables/useRemoteData.js';

const props = defineProps({
  projectId: {
    type: String,
    required: true
  },
  isCreator: {
    type: Boolean,
    default: false
  }
});

const updates = ref([]);
const newUpdate = ref('');
const showUpdateForm = ref(false);
const loading = ref(false);

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

const submitUpdate = async () => {
  if (!newUpdate.value.trim()) return;
  
  loading.value = true;
  try {
    await postUpdate({ content: newUpdate.value });
    await loadUpdates();
    newUpdate.value = '';
    showUpdateForm.value = false;
  } finally {
    loading.value = false;
  }
};

const loadUpdates = async () => {
  await fetchUpdates();
  updates.value = updatesData.value;
};

onMounted(() => {
  loadUpdates();
});
</script>
