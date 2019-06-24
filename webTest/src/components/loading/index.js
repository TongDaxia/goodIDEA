import Loading from './loading.vue'

export default {
    install(Vue) {
        let loading = null;

        Vue.loading = Vue.prototype.$loading = function(){
            return {
                open(opt ={}) {
                    if (!loading) {
                        loading = new (Vue.extend(Loading))();
                        loading.$mount();
                        document
                            .querySelector( typeof opt === 'string' ? 'body': (opt.container || 'body'))
                            .appendChild(loading.$el)
                    }

                    loading.text = typeof opt === 'string' ? opt : opt.text || '正在加载...';
                    loading.visible = true;
                },
                close(){
                    loading.visible = false;
                }
            }
        }()



    }
}