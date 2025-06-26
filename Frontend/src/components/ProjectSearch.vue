<template>
  <div class="card mb-4">
    <div class="card-body">
      <form @submit.prevent="applyFilters">
        <div class="row g-3">
          <div class="col-md-4">
            <label for="search" class="form-label">Search</label>
            <input
              type="text"
              id="search"
              v-model="filters.search"
              class="form-control"
              placeholder="Search projects..."
            />
          </div>
          
          <div class="col-md-3">
            <label for="category" class="form-label">Category</label>
            <select id="category" v-model="filters.category" class="form-select">
              <option value="">All Categories</option>
              <option value="technology">Technology</option>
              <option value="art">Art</option>
              <option value="music">Music</option>
              <option value="film">Film</option>
              <option value="games">Games</option>
              <option value="other">Other</option>
            </select>
          </div>
          
          <div class="col-md-3">
            <label for="sort" class="form-label">Sort By</label>
            <select id="sort" v-model="filters.sort" class="form-select">
              <option value="newest">Newest First</option>
              <option value="endingSoon">Ending Soon</option>
              <option value="mostFunded">Most Funded</option>
              <option value="trending">Trending</option>
            </select>
          </div>
          
          <div class="col-md-2 d-flex align-items-end">
            <button type="submit" class="btn btn-primary w-100">
              Apply Filters
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  initialFilters: {
    type: Object,
    default: () => ({
      search: '',
      category: '',
      sort: 'newest'
    })
  }
});

const emit = defineEmits(['filter-changed']);

const filters = ref({ ...props.initialFilters });

const applyFilters = () => {
  emit('filter-changed', { ...filters.value });
};

// Optional: Auto-apply filters after a delay when search changes
watch(() => filters.value.search, (newVal) => {
  const timeoutId = setTimeout(() => {
    applyFilters();
  }, 500);
  
  return () => clearTimeout(timeoutId);
});

// Auto-apply when category or sort changes
watch([() => filters.value.category, () => filters.value.sort], () => {
  applyFilters();
});
</script> 