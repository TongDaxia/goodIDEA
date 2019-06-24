<template>
    <div class="related-securities pannel">
        <div class="head-title">
            <a :href="'related_securities_detail' | detailLink({item:managerCode})" target="_blank"></a>
            <div class="title-box">
                <span class="title">管理人管理的产品</span>
                <span class="btn"></span>
            </div>
        </div>
        <table class="detail-table" v-if="relatedSecurities.length">
            <tr>
                <th>产品简称</th>
                <th>最新净值(元)</th>
                <th>成立以来收益(%)</th>
            </tr>
            <tr v-for="d in relatedSecurities">
                <td>
                    <span v-if="d.name === '--'">--</span>
                    <a :href="'index'|detailLink({code:d.id})" v-else style="color:#1482F0" target="_blank">{{d.name}}
                        <span class="red-start" v-if="d.flag === '1'">*</span>
                    </a>
                </td>
                <td>{{d.money}}</td>
                <td :style="d.profit | formatStyle">{{d.profit}}</td>
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
                relatedSecurities: [],//管理人管理的产品
                managerCode:'',
            }
        },
        created(){

        },
        methods: {
            loadData() {
                this.managerCode = JSON.parse(localStorage.getItem("localReq")).managerCode;
                const req = {...param, type: '', pagesize: 5, skip: 0, item: this.managerCode};
                this.$get('/finchinaAPP/nonStandard/getRelatedSecuritiesData.action', req).then(res => {
                    this.relatedSecurities = res.data.list;
                })
            }
        }
    }
</script>

<style scoped lang="less">
    .related-securities {
        .red-start {
            color: #EF3636;
        }
    }
</style>