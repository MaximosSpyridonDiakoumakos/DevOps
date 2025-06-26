import { createRouter, createWebHistory } from 'vue-router';
import { useApplicationStore } from '@/stores/application.js';

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/HomeView.vue')
  },
  {
    path: '/projects',
    name: 'projects',
    component: () => import('@/views/ProjectListView.vue')
  },
  {
    path: '/projects/:id',
    name: 'project-details',
    component: () => import('@/views/ProjectDetailsView.vue')
  },
  {
    path: '/projects/new',
    name: 'create-project',
    component: () => import('@/views/CreateProjectView.vue'),
    meta: { requiresCreator: true }
  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import('@/views/AdminDashboardView.vue'),
    meta: { requiresAdmin: true }
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('@/views/ProfileView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/logout',
    name: 'logout',
    component: () => import('@/views/LogoutView.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/LoginView.vue')
  },
  {
    path: '/signup',
    name: 'signup',
    component: () => import('@/views/SignupView.vue')
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Navigation guard
router.beforeEach((to, from, next) => {
  const store = useApplicationStore();

  // Redirect to login if accessing home page while not authenticated
  if (to.path === '/' && !store.isAuthenticated) {
    next('/login');
    return;
  }

  if (to.meta.requiresAdmin && store.userData?.role !== 'ADMIN') {
    next('/');
    return;
  }
  
  if (to.meta.requiresCreator && store.userData?.role !== 'CREATOR') {
    next('/');
    return;
  }
  
  if (to.meta.requiresAuth && !store.isAuthenticated) {
    next('/login');
    return;
  }
  
  next();
});

export default router;
