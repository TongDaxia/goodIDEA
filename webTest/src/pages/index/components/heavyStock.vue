<template>
    <div class="invisible-heavy-stock pannel">
        <div class="head-title">
            <a :href="'heavy_stock_detail' | detailLink({reportDate:heavyStock.reportDate})" target="_blank"></a>
            <div class="title-box">
                <p>
                    <span class="title">隐形重仓股</span>
                    <span v-if="heavyStock.reportDate" class="time">{{heavyStock.reportDate}}</span>
                </p>
                <span class="btn"></span>
            </div>
        </div>
        <table class="detail-table" v-if="heavyStock.list && heavyStock.list.length">
            <tr>
                <th>股票</th>
                <th>市值(万元)</th>
                <th>股数(万股)</th>
            </tr>
            <tr v-for="d in heavyStock.list">
                <td>{{d.name}}</td>
                <td>{{d.money}}</td>
                <td>{{d.num}}</td>
            </tr>
        </table>
        <p class="no-data" v-else>暂无相关信息</p>
    </div>
</template>

<script>
    import common from '@utils/commons';
    const param = common.getParams();
    export default {
        data() {
            return {
                heavyStock:{},//隐形重仓股
            }
        },
        created(){},
        methods: {
            loadData(){
                const req ={...param, pagesize:5, type:'outline'};
                this.$get('/finchinaAPP/nonStandard/getInvisibleHeavyStockData.action', req).then(res => {
                    this.heavyStock = res.data;
                })
            },
        }
    }
</script>

<style scoped lang="less">
    /* 隐形重仓股 */
    .invisible-heavy-stock {
        .head-title .time {
            font-size: 0.26rem;
            color: #8C8C8C;
            margin-left: 0.08rem;
        }
    }
</style>