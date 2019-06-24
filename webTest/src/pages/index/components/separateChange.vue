<template>
    <div class="shareChange pannel">
        <div class="head-title">
            <a :href="'separate_change_detail' | detailLink" target="_blank"></a>
            <div class="title-box">
                <span class="title">份额变动</span>
                <span class="btn"></span>
            </div>
        </div>
        <div class="echart-box">
            <div id="shareChangeChart" v-if="!isEmpty"></div>
            <p class="no-data" v-else>暂无相关信息</p>
        </div>
    </div>
</template>

<script>
    import echarts from 'echarts/dist/echarts.common';
    import common from '@utils/commons';
    const param = common.getParams();

    export default {
        data() {
            return {
                isEmpty: false,
            }
        },
        created(){},
        mounted(){},
        methods: {
            loadData(){
                const req ={...param, pagesize: 4};
                this.$get('/finchinaAPP/nonStandard/getShareChange.action', req).then(res => {
                    this.initTwoChart(res.data.xData, res.data.yData);
                }).catch(err=>{
                    this.isEmpty = true;
                });
            },
            initTwoChart(x,y) {
                if (!this.myTwoCharts) this.myTwoCharts = echarts.init(document.getElementById('shareChangeChart'));
                const option = {
                    grid: {top: '15%', left: '-10%', right: '3%',
                        bottom: '0%', containLabel: true},
                    xAxis: {
                        type: 'category',
                        data: x,
                        axisTick:false,
                        axisLine:{show:false},
                        axisLabel: {
                            interval: 0,
                            textStyle: {
                                fontSize: 10,
                                color:'#8c8c8c'
                            }
                        }
                    },
                    yAxis: {
                        type: 'value',
                        show:false
                    },
                    series: [{
                        data: y,
                        type: 'bar',
                        barWidth: '45%',
                        itemStyle: {
                            normal: {
                                color: function(params) {
                                    const colors=['#F68E8D','#F9C880','#6CDBDF','#9CA3FF'];
                                    return colors[params.dataIndex]
                                },
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: function(v){
                                        if (v.value){
                                            let [sInt, sFloat] = (v.value + '').split('.');
                                            if (sInt.length > 4) {
                                                [sInt, sFloat] = ((v.value/10000).toFixed(2) + '').split('.');
                                                sInt = sInt.replace(/\d{1,3}(?=(\d{3})+$)/g, '$&,');//千位符
                                                return (sFloat ? `${sInt}.${sFloat}` : `${sInt}`) + '亿份';
                                            }else{
                                                sInt = sInt.replace(/\d{1,3}(?=(\d{3})+$)/g, '$&,');
                                                return (sFloat ? `${sInt}.${sFloat}` : `${sInt}`) + '万份';
                                            }
                                        }else{
                                            return '';
                                        }
                                    },
                                    textStyle: {fontSize: 11, color:'#5C5C5C'}
                                }
                            }
                        },
                    }]
                };
                this.myTwoCharts.setOption(option, true);
            },
        }
    }
</script>

<style scoped lang="less">
    .shareChange{
        .echart-box{
            position: relative;
            #shareChangeChart{
                height: 3.8rem;
            }
        }
    }
</style>