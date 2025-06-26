<template>
  <div class="bg-body-tertiary">
    <div class="container">
      <div class="row py-4 px-3">
        <div class="col-12">
          <h1 class="fs-3">Admin Dashboard</h1>
          
          <ul class="nav nav-tabs mb-4">
            <li class="nav-item">
              <button 
                class="nav-link" 
                :class="{ active: activeTab === 'pending' }"
                @click="activeTab = 'pending'"
              >
                Pending Projects
              </button>
            </li>
            <li class="nav-item">
              <button 
                class="nav-link" 
                :class="{ active: activeTab === 'approved' }"
                @click="activeTab = 'approved'"
              >
                Approved Projects
              </button>
            </li>
            <li class="nav-item">
              <button 
                class="nav-link" 
                :class="{ active: activeTab === 'users' }"
                @click="activeTab = 'users'"
              >
                User Management
              </button>
            </li>
          </ul>

          <div v-if="loading" class="text-center py-4">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
          </div>

          <div v-else-if="activeTab === 'pending'" class="row">
            <div 
              v-for="project in pendingProjects" 
              :key="project.id" 
              class="col-md-4 mb-4"
            >
              <ProjectCard 
                :project="project"
                :show-status="true"
              >
                <template #actions>
                  <button 
                    class="btn btn-success me-2"
                    @click="approveProject(project.id)"
                  >
                    Approve
                  </button>
                  <button 
                    class="btn btn-danger"
                    @click="rejectProject(project.id)"
                  >
                    Reject
                  </button>
                </template>
              </ProjectCard>
            </div>
            <div v-if="!pendingProjects.length" class="text-center py-5">
              <p>No pending projects</p>
            </div>
          </div>

          <div v-else-if="activeTab === 'approved'" class="row">
            <div 
              v-for="project in approvedProjects" 
              :key="project.id" 
              class="col-md-4 mb-4"
            >
              <ProjectCard :project="project" :show-status="true" />
            </div>
            <div v-if="!approvedProjects.length" class="text-center py-5">
              <p>No approved projects</p>
            </div>
          </div>

          <div v-else class="row">
            <div class="col-12">
              <div class="table-responsive">
                <table class="table">
                  <thead>
                    <tr>
                      <th>Username</th>
                      <th>Email</th>
                      <th>Role</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="user in users" :key="user.id">
                      <td>{{ user.username }}</td>
                      <td>{{ user.email }}</td>
                      <td>
                        <select 
                          v-model="user.role" 
                          class="form-select form-select-sm"
                          @change="updateUserRole(user.id, user.role)"
                        >
                          <option value="ROLE_ADMIN">Admin</option>
                          <option value="ROLE_CREATOR">Creator</option>
                          <option value="ROLE_SUPPORTER">Supporter</option>
                        </select>
                      </td>
                      <td>
                        <span 
                          class="badge"
                          :class="user.active ? 'bg-success' : 'bg-danger'"
                        >
                          {{ user.active ? 'Active' : 'Inactive' }}
                        </span>
                      </td>
                      <td>
                        <button 
                          class="btn btn-sm"
                          :class="user.active ? 'btn-danger' : 'btn-success'"
                          @click="toggleUserStatus(user.id)"
                        >
                          {{ user.active ? 'Deactivate' : 'Activate' }}
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRemoteData } from '@/composables/useRemoteData.js';
import ProjectCard from '@/components/ProjectCard.vue';

const activeTab = ref('pending');
const loading = ref(true);
const pendingProjects = ref([]);
const approvedProjects = ref([]);
const users = ref([]);

const { data: pendingData, performRequest: fetchPending } = useRemoteData('/api/projects/pending', true);
const { data: approvedData, performRequest: fetchApproved } = useRemoteData('/api/projects/approved', true);
const { data: usersData, performRequest: fetchUsers } = useRemoteData('/api/users', true);
const { performRequest: updateRole } = useRemoteData(userId => `/api/users/${userId}/role`, true, 'PUT');
const { performRequest: toggleStatus } = useRemoteData(userId => `/api/users/${userId}/toggle-status`, true, 'PUT');

const approveProject = async (projectId) => {
  if (!confirm('Are you sure you want to approve this project?')) return;
  
  try {
    await approveProjectRequest(projectId);
    await loadProjects();
  } catch (error) {
    console.error('Failed to approve project:', error);
    alert('Failed to approve project. Please try again.');
  }
};

const rejectProject = async (projectId) => {
  if (!confirm('Are you sure you want to reject this project?')) return;
  
  try {
    await rejectProjectRequest(projectId);
    await loadProjects();
  } catch (error) {
    console.error('Failed to reject project:', error);
    alert('Failed to reject project. Please try again.');
  }
};

const updateUserRole = async (userId, newRole) => {
  try {
    await updateRole(userId, { role: newRole });
  } catch (error) {
    console.error('Failed to update user role:', error);
    alert('Failed to update user role. Please try again.');
  }
};

const toggleUserStatus = async (userId) => {
  try {
    await toggleStatus(userId);
    await loadUsers();
  } catch (error) {
    console.error('Failed to toggle user status:', error);
    alert('Failed to update user status. Please try again.');
  }
};

const loadData = async () => {
  loading.value = true;
  try {
    if (activeTab.value === 'users') {
      await fetchUsers();
      users.value = usersData.value;
    } else {
      await Promise.all([fetchPending(), fetchApproved()]);
      pendingProjects.value = pendingData.value;
      approvedProjects.value = approvedData.value;
    }
  } finally {
    loading.value = false;
  }
};

watch(activeTab, loadData);

onMounted(() => {
  loadData();
});
</script> 