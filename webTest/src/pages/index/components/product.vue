<template>
    <div class="product-introduce pannel">
        <div class="head-title">
            <a :href="'product_detail' | detailLink" target="_blank"></a>
            <div class="title-box">
                <span class="title">产品概况</span>
                <span class="btn"></span>
            </div>
        </div>
        <div class="product-introduce-detail">
            <template v-for="(v,k) in product.item">
                <p v-if="k==='管理人'"><span>{{k}}</span>
                    <span v-html="showLinks({name: v,code: product.code['管理人代码'], type: 'company'})"></span>
                </p>
                <p v-else-if="k==='投资顾问'"><span>投资顾问</span>
                    <span v-html="showLinks({name: v,code: product.code['投资顾问代码'], type: 'company'})"></span>
                <p v-else-if="k==='受托人'"><span>受托人</span>
                    <span v-html="showLinks({name: v,code: product.code['受托人代码'], type: 'company'})"></span></p>
                <p v-else><span>{{k}}</span><span class=title>{{v}}</span></p>
            </template>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    const param = common.getParams();
    export default {
        data() {
            return {
                product:{},
            }
        },
        created(){},
        methods: {
            loadData(){
                const req ={...param, type:'outline'};
                this.$get('/finchinaAPP/nonStandard/getProductOverview.action', req).then(res => {
                    this.product = res.data;
                })
            },
            showLinks(obj){
                return common.showLinks(obj);
            },
        }
    }
</script>

<style scoped lang="less">
    .product-introduce {
        font-size: 0.24rem;
        color: #111111;
        .product-introduce-detail {
            padding: 0.3rem 0.32rem 0 0.32rem;
            font-size: 0.26rem;
            .title {
                font-size: 0.28rem;
                flex: 1;
                text-align: right;
            }
        }
        p {
            display: flex;
            justify-content: space-between;
            margin-bottom: 0.3rem;
            &:last-child{
                margin-bottom: 0;
            }
        }
        p > span:first-child {
            color: #8c8c8c;
            margin-right: 1rem;
            font-size: 0.26rem;
        }
    }
</style>