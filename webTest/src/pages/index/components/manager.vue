<template>
    <div class="manager pannel">
        <div class="head-title">
            <a :href="'manager_detail' | detailLink({total:manager.totalCount})" target="_blank"></a>
            <div class="title-box">
                <span class="title">投资经理</span>
                <p>
                    <span class="people-number" v-if="manager.remark">{{manager.remark}}</span>
                    <span class="btn"></span>
                </p>
            </div>
        </div>
        <div class="manager-detail" v-if="manager.list && manager.list.length">
            <div class="item" v-for="d in manager.list">
                <div class="people-photo">
                    <img v-if="d.image" :src="d.image"/>
                    <img src="../../../assets/img/people.png" v-else />
                </div>
                <div class="people-info">
                    <div  class="name">{{d.name}}</div>
                    <div class="item-box">
                        <div class="text-item">
                            <p class="label">任期</p>
                            <p class="time">{{d.startDate}}~{{d.endDate}}</p>
                        </div>
                        <div class="text-item">
                            <p class="label">任期回报</p>
                            <p :style="d.returnOfTerm | formatStyle">{{d.returnOfTerm | noEmpty('%')}}</p>
                        </div>
                        <div class="text-item">
                            <p class="label">任期沪深300</p>
                            <p :style="d.returnOfSS300ByTerm | formatStyle">{{d.returnOfSS300ByTerm | noEmpty('%')}}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <p class="no-data" v-else>暂无相关信息</p>
    </div>
</template>

<script>
    import common from '@utils/commons';
    let param = common.getParams();
    export default {
        data() {
            return {
                manager:{},
            }
        },
        created(){

        },
        methods: {
            //投资经理
            loadData(){
                this.$get('/finchinaAPP/nonStandard/getManagerList.action', param).then(res => {
                    this.manager = res.data;
                })
            },
        }
    }
</script>

<style scoped lang="less">
    /* 投资经理 */
    .manager{
        .people-number {
            font-size: 0.26rem;
            color: #8c8c8c;
            margin-right: -0.04rem;
        }
        .manager-detail {
            border-top: 0.01rem solid #E6E6E6;
            margin-top: 0.3rem;
            .item {
                padding: 0.27rem 0.32rem;
                border-bottom: 0.01rem solid #E6E6E6;
                &:last-child{
                    padding-bottom: 0;
                    border-bottom: 0;
                }
            }
        }
        .people-photo {
            display: inline-block;
            width: 1.28rem;
            height: 1.28rem;
            background: #59b4e0;
            border-radius: 50%;
            overflow: hidden;
            margin-right: 0.32rem;
            img {
                display: inline-block;
                width: 100%;
            }
        }
        .people-info {
            display: inline-block;
            width: calc(100% - 1.68rem);
            vertical-align: top;
            .item-box {
                display: flex;
                justify-content: space-between;
                .text-item {
                    padding-top: 0.2rem;
                    font-size: 0.26rem;
                }
            }
            .name {
                font-size: 0.3rem;
                color: #333333;
                padding-top: 0.05rem;
                font-weight: 600;
            }
            .label {
                color: #8C8C8C;
                font-size: 0.2rem;
                padding-bottom: 0.16rem;
            }
            .time {
                color: #333333;
                justify-content: flex-end;
            }
            .value {
                color: #129012;
            }
        }
    }
</style>