<template>
    <div class="head-number">
        <div class="item" v-if="type == 2">
            <p class="number" :style="topNum.tenThousandProceeds | formatStyle(1)">{{topNum.tenThousandProceeds | noEmpty}}</p>
            <p class="text">万份收益<span v-if="topNum.endDate">({{topNum.endDate}})</span></p>
        </div>
        <div class="item" v-else>
            <p class="number" :style="topNum['netValue'] | formatStyle(1)">{{topNum['netValue'] | noEmpty}}</p>
            <p class="text">净值<span v-if="topNum.endDate">({{topNum.endDate}})</span></p>
        </div>
        <div class="item" v-if="type == 2">
            <p class="number" :style="topNum['sevenDayYearProceeds'] | formatStyle(1)">{{topNum['sevenDayYearProceeds'] | noEmpty('%')}}</p>
            <p class="text">7日年化收益</p>
        </div>
        <div class="item" v-else>
            <p class="number" :style="topNum.latestGrowth | formatStyle(1)">{{topNum.latestGrowth | noEmpty('%')}}</p>
            <p class="text">涨跌幅</p>
        </div>
        <div class="item" v-if="!(from ==1 && type == 2)">
            <p class="number" :style="topNum.totalGrowth | formatStyle(1)">{{topNum.totalGrowth | noEmpty('%')}}</p>
            <p class="text">成立至今</p>
        </div>
        <div class="item">
            <p class="last-text number" :style="{fontWeight:topNum.raiseScale? '600':'normal'}">
                {{topNum.raiseScale | noEmpty}}
                <span class="last-unit" v-if="topNum.pfScale_unit">{{topNum.pfScale_unit}}</span>
            </p>
            <p class="text">规模<span v-if="topNum.establishDate">({{topNum.raiseScale ? topNum.establishDate:'--'}})</span></p>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    let param = common.getParams();
    export default {
        data(){
            return {
                type:1,
                from:param.from,
                topNum:{},//头部数据
            }
        },
        methods:{
            loadData(){
                return new Promise(resolve => {
                    this.$get('/finchinaAPP/nonStandard/getHeadInfo.action', param).then((res) => {
                        this.topNum = res.data;
                        this.type = res.data.isCurrencyType;
                        resolve(res.data);
                    }).catch(err => {
                        resolve();
                    });
                })
            },
        }
    }
</script>

<style scoped lang="less">
    .head-number {
        display: flex;
        background: #fff;
        color: #333;
        padding: 0.36rem 0.26rem;
        align-items: flex-end;
        .item{
            flex: 1;
            font-size: 0.2rem;
            text-align: center;
            white-space: nowrap;
            .text{
                color: #8c8c8c;
                margin-top: 0.12rem;
                line-height: 0.28rem;
            }
            .number {
                font-size: 0.32rem;
                line-height: 0.32rem;
            }
            .last-text{
                color: #333333 !important;
                font-weight: 600;
                .last-unit{
                    font-size: 0.2rem;
                    font-weight: normal;
                    color: #151515;
                    line-height: 0.32rem;
                    vertical-align: bottom;
                    margin-left: -0.08rem;
                    display: inline-block;
                    margin-bottom: -0.02rem;
                }
            }
        }
    }
</style>