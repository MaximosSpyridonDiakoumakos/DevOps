<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useApplicationStore } from '@/stores/application.js';

const router = useRouter();
const { setUserData, persistUserData } = useApplicationStore();

const loading = ref(false);
const signupFailed = ref(false);
const errorMessage = ref('');

const formData = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  role: 'ROLE_SUPPORTER' // Default role
});

const onFormSubmit = () => {
  if (formData.value.password !== formData.value.confirmPassword) {
    signupFailed.value = true;
    errorMessage.value = 'Passwords do not match';
    return;
  }

  loading.value = true;
  signupFailed.value = false;

  fetch('/api/api/auth/signup', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(formData.value)
  })
      .then(async (response) => {
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
          const data = await response.json();
          if (response.ok) {
            router.push({ name: 'login' });
          } else {
            throw new Error(data.message || 'Registration failed');
          }
        } else if (response.ok) {
          router.push({ name: 'login' });
        } else {
          throw new Error('Registration failed');
        }
      })
      .catch((err) => {
        console.warn(err);
        signupFailed.value = true;
        errorMessage.value = err.message || 'Registration failed';
      })
      .finally(() => {
        loading.value = false;
      });
};
</script>

<template>
  <div class="bg-body-tertiary">
    <div class="container">
      <div class="row py-4 px-3">
        <div class="col-4">
          <div class="mb-4">
            <h1 class="fs-3">Sign Up</h1>
          </div>
          <div class="spinner-border" role="status" v-if="loading">
            <span class="visually-hidden">Loading...</span>
          </div>
          <form v-else @submit.prevent="onFormSubmit">
            <div class="mb-2" v-if="signupFailed">
              <div class="alert alert-danger" role="alert">
                {{ errorMessage }}
              </div>
            </div>
            <div class="mb-3">
              <label for="usernameInput" class="form-label">Username</label>
              <input
                  v-model="formData.username"
                  type="text"
                  class="form-control"
                  id="usernameInput"
                  required
              />
            </div>
            <div class="mb-3">
              <label for="emailInput" class="form-label">Email address</label>
              <input
                  v-model="formData.email"
                  type="email"
                  class="form-control"
                  id="emailInput"
                  required
              />
            </div>
            <div class="mb-3">
              <label for="passwordInput" class="form-label">Password</label>
              <input
                  v-model="formData.password"
                  type="password"
                  class="form-control"
                  id="passwordInput"
                  required
              />
            </div>
            <div class="mb-3">
              <label for="confirmPasswordInput" class="form-label">Confirm Password</label>
              <input
                  v-model="formData.confirmPassword"
                  type="password"
                  class="form-control"
                  id="confirmPasswordInput"
                  required
              />
            </div>
            <div class="mb-3">
              <label for="roleSelect" class="form-label">Role</label>
              <select v-model="formData.role" class="form-select" id="roleSelect" required>
                <option value="ROLE_SUPPORTER">Supporter</option>
                <option value="ROLE_CREATOR">Creator</option>
              </select>
            </div>
            <div class="d-flex gap-2">
              <button type="submit" class="btn btn-primary">Sign Up</button>
              <RouterLink to="/login" class="btn btn-outline-secondary">Back to Login</RouterLink>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>