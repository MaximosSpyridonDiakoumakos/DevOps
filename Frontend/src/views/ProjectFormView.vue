<template>
  <div class="bg-body-tertiary">
    <div class="container">
      <div class="row py-4 px-3">
        <div class="col-12">
          <div class="mb-4">
            <RouterLink class="small" :to="{ name: 'projects' }">Back to Projects</RouterLink>
            <h1 class="fs-3">{{ isEditing ? 'Edit Project' : 'Create New Project' }}</h1>
          </div>

          <div class="card">
            <div class="card-body">
              <form @submit.prevent="handleSubmit">
                <div class="mb-3">
                  <label for="title" class="form-label">Project Title</label>
                  <input
                    type="text"
                    id="title"
                    v-model="form.title"
                    class="form-control"
                    :class="{ 'is-invalid': errors.title }"
                    required
                  />
                  <div class="invalid-feedback">{{ errors.title }}</div>
                </div>

                <div class="mb-3">
                  <label for="description" class="form-label">Description</label>
                  <textarea
                    id="description"
                    v-model="form.description"
                    class="form-control"
                    :class="{ 'is-invalid': errors.description }"
                    rows="5"
                    required
                  ></textarea>
                  <div class="invalid-feedback">{{ errors.description }}</div>
                </div>

                <div class="row mb-3">
                  <div class="col-md-6">
                    <label for="fundingGoal" class="form-label">Funding Goal ($)</label>
                    <input
                      type="number"
                      id="fundingGoal"
                      v-model.number="form.fundingGoal"
                      class="form-control"
                      :class="{ 'is-invalid': errors.fundingGoal }"
                      min="1"
                      required
                    />
                    <div class="invalid-feedback">{{ errors.fundingGoal }}</div>
                  </div>
                  
                  <div class="col-md-6">
                    <label for="deadline" class="form-label">Deadline</label>
                    <input
                      type="date"
                      id="deadline"
                      v-model="form.deadline"
                      class="form-control"
                      :class="{ 'is-invalid': errors.deadline }"
                      :min="minDate"
                      required
                    />
                    <div class="invalid-feedback">{{ errors.deadline }}</div>
                  </div>
                </div>

                <div class="mb-3">
                  <label for="category" class="form-label">Category</label>
                  <select
                    id="category"
                    v-model="form.category"
                    class="form-select"
                    :class="{ 'is-invalid': errors.category }"
                    required
                  >
                    <option value="">Select a category</option>
                    <option value="technology">Technology</option>
                    <option value="art">Art</option>
                    <option value="music">Music</option>
                    <option value="film">Film</option>
                    <option value="games">Games</option>
                    <option value="other">Other</option>
                  </select>
                  <div class="invalid-feedback">{{ errors.category }}</div>
                </div>

                <div class="d-flex gap-2">
                  <button 
                    type="submit" 
                    class="btn btn-primary"
                    :disabled="loading"
                  >
                    {{ isEditing ? 'Update Project' : 'Create Project' }}
                  </button>
                  <RouterLink 
                    :to="{ name: 'projects' }" 
                    class="btn btn-secondary"
                  >
                    Cancel
                  </RouterLink>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useRemoteData } from '@/composables/useRemoteData.js';

const route = useRoute();
const router = useRouter();
const isEditing = computed(() => !!route.params.id);

const form = ref({
  title: '',
  description: '',
  fundingGoal: null,
  deadline: '',
  category: ''
});

const errors = ref({});
const loading = ref(false);

const minDate = computed(() => {
  const today = new Date();
  today.setDate(today.getDate() + 1);
  return today.toISOString().split('T')[0];
});

const { data, performRequest: fetchProject } = useRemoteData(
  `/api/projects/${route.params.id}`,
  true
);

const { performRequest: saveProject } = useRemoteData(
  isEditing.value 
    ? `/api/projects/${route.params.id}`
    : '/api/projects',
  true,
  isEditing.value ? 'PUT' : 'POST'
);

const validateForm = () => {
  const newErrors = {};
  
  if (!form.value.title?.trim()) {
    newErrors.title = 'Title is required';
  }
  
  if (!form.value.description?.trim()) {
    newErrors.description = 'Description is required';
  }
  
  if (!form.value.fundingGoal || form.value.fundingGoal <= 0) {
    newErrors.fundingGoal = 'Please enter a valid funding goal';
  }
  
  if (!form.value.deadline) {
    newErrors.deadline = 'Deadline is required';
  } else {
    const deadlineDate = new Date(form.value.deadline);
    const today = new Date();
    if (deadlineDate <= today) {
      newErrors.deadline = 'Deadline must be in the future';
    }
  }
  
  if (!form.value.category) {
    newErrors.category = 'Please select a category';
  }
  
  errors.value = newErrors;
  return Object.keys(newErrors).length === 0;
};

const handleSubmit = async () => {
  if (!validateForm()) return;
  
  loading.value = true;
  try {
    await saveProject(form.value);
    router.push({ name: 'projects' });
  } catch (error) {
    console.error('Failed to save project:', error);
    alert('Failed to save project. Please try again.');
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  if (isEditing.value) {
    loading.value = true;
    try {
      await fetchProject();
      form.value = { ...data.value };
    } finally {
      loading.value = false;
    }
  }
});
</script> 