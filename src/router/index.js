import Vue from 'vue'
import VueRouter from 'vue-router'
import CodeChallenge from '../views/CodeChallenge.vue'

Vue.use(VueRouter);

  const routes = [
  {
    path: '/',
    name: 'CodeChallenge',
    component: CodeChallenge
  },
  {
    path: '/score',
    name: 'Scoreboard',
    component: () => import('../views/Scoreboard.vue')
  }
];

const router = new VueRouter({
  routes
});

export default router
