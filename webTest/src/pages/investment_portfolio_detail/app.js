import Vue from 'vue'
import App from './app.vue'

import { http } from '../../plugins/http';
Vue.use(http);

import FastClick from 'fastclick'
FastClick.attach(document.body);

import Loading from 'components/loading'
Vue.use(Loading);

import filters from '@utils/filters';
Object.keys(filters).forEach(key => {
    Vue.filter(key, filters[key])
})

new Vue({
    el: '#app',
    render: h => h(App)
});
