<template>
    <div class="shareBonus pannel">
        <div class="head-title">
            <a :href="'separate_bonus_detail' | detailLink" target="_blank"></a>
            <div class="title-box">
                <span class="title">分红</span>
                <p>
                    <span class="text" v-if="shareBonus.remark">{{shareBonus.remark}}</span>
                    <span class="btn"></span>
                </p>
            </div>
        </div>
        <div class="shareBonus-detail" v-if="JSON.stringify(shareBonus) !== '{}'">
            <p class="item">
                <span>最新单位分红</span>
                <span>{{shareBonus.perBonus}}</span>
            </p>
            <p class="item">
                <span>权益登记日</span>
                <span>{{shareBonus.rightsRegisterDate}}</span>
            </p>
            <p class="item">
                <span>除息日</span>
                <span>{{shareBonus.exDividendDate}}</span>
            </p>
            <p class="item">
                <span>红利发放日</span>
                <span>{{shareBonus.bonusReleaseDate}}</span>
            </p>
        </div>
        <p class="no-data" v-else>暂无相关信息</p>
    </div>
</template>

<script>
    import common from '@utils/commons';
    const param = common.getParams();

    export default {
        data(){
            return {
                shareBonus:{},//分红
            }
        },
        methods:{
            loadData(){
                this.$get('/finchinaAPP/nonStandard/getShareOutBonus.action', param).then(res => {
                    this.shareBonus = res.data;
                });
            },
        }

    }
</script>

<style scoped lang="less">
    .shareBonus{
        .head-title .text{
            font-size: 0.26rem;
            color: #8C8C8C;
            line-height: 0.26rem;
        }
        .shareBonus-detail{
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            padding: 0.06rem 0.32rem 0 0.32rem;
            .item{
                display: flex;
                width: 3.1rem;
                margin-top: 0.3rem;
                justify-content: space-between;
                span{
                    font-size: 0.26rem;
                    line-height: 0.26rem;
                    &:first-child{color: #8C8C8C;}
                    &:last-child{color: #111111;}
                }
            }
        }
    }
</style>