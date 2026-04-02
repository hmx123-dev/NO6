<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							预约编号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.yuyuebianhao" placeholder="预约编号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							医生账号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.yishengzhanghao" placeholder="医生账号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							用户账号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.zhanghao" placeholder="用户账号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							发送状态：
						</div>
						<div class="search_box">
							<el-select v-model="searchQuery.fasongzhuangtai" placeholder="请选择" clearable>
								<el-option label="待发送" :value="0"></el-option>
								<el-option label="发送成功" :value="1"></el-option>
								<el-option label="发送失败" :value="2"></el-option>
							</el-select>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
						<el-button class="search_btn" @click="resetClick()" size="small">重置</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="warning" @click="retryBatchClick" :disabled="selRows.length===0">
						<i class="iconfont icon-zhongshi"></i>
						批量重试
					</el-button>
					<el-button class="add_btn" type="success" @click="statisticsClick">
						<i class="iconfont icon-tongji"></i>
						统计
					</el-button>
				</div>
			</div>
			<el-table
				v-loading="listLoading"
				border
				:stripe='false'
				@selection-change="handleSelectionChange"
				ref="table"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="yuyuebianhao"
					label="预约编号">
					<template #default="scope">
						{{scope.row.yuyuebianhao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="yishengzhanghao"
					label="医生账号">
					<template #default="scope">
						{{scope.row.yishengzhanghao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="zhanghao"
					label="用户账号">
					<template #default="scope">
						{{scope.row.zhanghao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					label="通知类型">
					<template #default="scope">
						<el-tag v-if="scope.row.tongzhileixing===1" type="success">预约成功通知</el-tag>
						<el-tag v-else-if="scope.row.tongzhileixing===2" type="warning">就诊前一天提醒</el-tag>
						<el-tag v-else-if="scope.row.tongzhileixing===3" type="primary">就诊当天提醒</el-tag>
						<el-tag v-else-if="scope.row.tongzhileixing===4" type="info">就诊后随访提醒</el-tag>
						<el-tag v-else>未知类型</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="200"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="tongzhineirong"
					label="通知内容"
					show-overflow-tooltip>
					<template #default="scope">
						{{scope.row.tongzhineirong}}
					</template>
				</el-table-column>
				<el-table-column min-width="160"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="jihuafasongshijian"
					label="计划发送时间">
					<template #default="scope">
						{{scope.row.jihuafasongshijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="160"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="fasongshijian"
					label="实际发送时间">
					<template #default="scope">
						{{scope.row.fasongshijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					label="发送状态">
					<template #default="scope">
						<el-tag v-if="scope.row.fasongzhuangtai===0" type="info">待发送</el-tag>
						<el-tag v-else-if="scope.row.fasongzhuangtai===1" type="success">发送成功</el-tag>
						<el-tag v-else-if="scope.row.fasongzhuangtai===2" type="danger">发送失败</el-tag>
						<el-tag v-else>未知</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="100"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="chongshicishu"
					label="重试次数">
					<template #default="scope">
						{{scope.row.chongshicishu}}
					</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					label="接收状态">
					<template #default="scope">
						<el-tag v-if="scope.row.jieshouzhuangtai===0" type="info">未接收</el-tag>
						<el-tag v-else-if="scope.row.jieshouzhuangtai===1" type="warning">已接收</el-tag>
						<el-tag v-else-if="scope.row.jieshouzhuangtai===2" type="success">已读</el-tag>
						<el-tag v-else>未知</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="160"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="jieshoushijian"
					label="接收时间">
					<template #default="scope">
						{{scope.row.jieshoushijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="200"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="shibaiyuanyin"
					label="失败原因"
					show-overflow-tooltip>
					<template #default="scope">
						<span v-if="scope.row.shibaiyuanyin" style="color: #f56c6c;">{{scope.row.shibaiyuanyin}}</span>
						<span v-else>-</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="200" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" @click="infoClick(scope.row.id)">
							<i class="iconfont icon-sousuo2"></i>
							查看
						</el-button>
						<el-button v-if="scope.row.fasongzhuangtai===2" class="edit_btn" type="warning" @click="retryClick(scope.row.id)">
							<i class="iconfont icon-zhongshi"></i>
							重试
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				background
				:layout="layouts.join(',')"
				:total="total"
				:page-size="listQuery.limit"
                v-model:current-page="listQuery.page"
				prev-text="上一页"
				next-text="下一页"
				:hide-on-single-page="false"
				:style='{}'
				:page-sizes="[10, 20, 30, 40, 50, 100]"
				@size-change="sizeChange"
				@current-change="currentChange"  />
		</div>
		
		<!-- 统计弹窗 -->
		<el-dialog v-model="statisticsVisible" title="通知统计" width="500px">
			<el-row :gutter="20">
				<el-col :span="12">
					<el-card class="statistics-card pending">
						<div class="statistics-number">{{statistics.pendingCount}}</div>
						<div class="statistics-label">待发送</div>
					</el-card>
				</el-col>
				<el-col :span="12">
					<el-card class="statistics-card success">
						<div class="statistics-number">{{statistics.successCount}}</div>
						<div class="statistics-label">发送成功</div>
					</el-card>
				</el-col>
			</el-row>
			<el-row :gutter="20" style="margin-top: 20px;">
				<el-col :span="12">
					<el-card class="statistics-card fail">
						<div class="statistics-number">{{statistics.failCount}}</div>
						<div class="statistics-label">发送失败</div>
					</el-card>
				</el-col>
				<el-col :span="12">
					<el-card class="statistics-card total">
						<div class="statistics-number">{{statistics.totalCount}}</div>
						<div class="statistics-label">总计</div>
					</el-card>
				</el-col>
			</el-row>
		</el-dialog>
	</div>
</template>

<script setup>
	import axios from 'axios'
    import moment from "moment"
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
		watch,
		computed,
	} from 'vue'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useStore
	} from 'vuex'

	const store = useStore()
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const router = useRouter()
	const route = useRoute()

	// 基础数据
	const list = ref([])
	const listLoading = ref(false)
	const listQuery = reactive({
		page: 1,
		limit: 10
	})
	const total = ref(0)
	const layouts = reactive(["total","prev","pager","next","sizes","jumper"])
	const searchQuery = reactive({
		yuyuebianhao: '',
		yishengzhanghao: '',
		zhanghao: '',
		fasongzhuangtai: null
	})
	const selRows = ref([])
	const statisticsVisible = ref(false)
	const statistics = reactive({
		pendingCount: 0,
		successCount: 0,
		failCount: 0,
		totalCount: 0
	})

	// 获取列表
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery))
		if (searchQuery.yuyuebianhao) {
			params['yuyuebianhao'] = searchQuery.yuyuebianhao
		}
		if (searchQuery.yishengzhanghao) {
			params['yishengzhanghao'] = searchQuery.yishengzhanghao
		}
		if (searchQuery.zhanghao) {
			params['zhanghao'] = searchQuery.zhanghao
		}
		if (searchQuery.fasongzhuangtai !== null && searchQuery.fasongzhuangtai !== '') {
			params['fasongzhuangtai'] = searchQuery.fasongzhuangtai
		}
		context?.$http({
			url: 'tongzhijilu/page',
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = res.data.data.total
		})
	}

	// 搜索
	const searchClick = () => {
		listQuery.page = 1
		getList()
	}

	// 重置
	const resetClick = () => {
		searchQuery.yuyuebianhao = ''
		searchQuery.yishengzhanghao = ''
		searchQuery.zhanghao = ''
		searchQuery.fasongzhuangtai = null
		listQuery.page = 1
		getList()
	}

	// 分页
	const sizeChange = (size) => {
		listQuery.limit = size
		getList()
	}

	const currentChange = (page) => {
		listQuery.page = page
		getList()
	}

	// 选择行
	const handleSelectionChange = (rows) => {
		selRows.value = rows
	}

	// 查看详情
	const infoClick = (id) => {
		context?.$http({
			url: `tongzhijilu/info/${id}`,
			method: 'get'
		}).then(res => {
			if (res.data.code === 0) {
				ElMessageBox.alert(
					`<div style="line-height: 1.8;">
						<p><strong>预约编号：</strong>${res.data.data.yuyuebianhao || '-'}</p>
						<p><strong>医生账号：</strong>${res.data.data.yishengzhanghao || '-'}</p>
						<p><strong>用户账号：</strong>${res.data.data.zhanghao || '-'}</p>
						<p><strong>通知类型：</strong>${getTongzhileixingName(res.data.data.tongzhileixing)}</p>
						<p><strong>通知内容：</strong>${res.data.data.tongzhineirong || '-'}</p>
						<p><strong>计划发送时间：</strong>${res.data.data.jihuafasongshijian || '-'}</p>
						<p><strong>实际发送时间：</strong>${res.data.data.fasongshijian || '-'}</p>
						<p><strong>发送状态：</strong>${getFasongzhuangtaiName(res.data.data.fasongzhuangtai)}</p>
						<p><strong>重试次数：</strong>${res.data.data.chongshicishu || 0}</p>
						<p><strong>接收状态：</strong>${getJieshouzhuangtaiName(res.data.data.jieshouzhuangtai)}</p>
						<p><strong>接收时间：</strong>${res.data.data.jieshoushijian || '-'}</p>
						<p><strong>失败原因：</strong><span style="color: #f56c6c;">${res.data.data.shibaiyuanyin || '-'}</span></p>
					</div>`,
					'通知详情',
					{ dangerouslyUseHTMLString: true }
				)
			}
		})
	}

	// 重试发送
	const retryClick = (id) => {
		ElMessageBox.confirm('确定要重试发送该通知吗？', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		}).then(() => {
			context?.$http({
				url: `tongzhijilu/retry/${id}`,
				method: 'post'
			}).then(res => {
				if (res.data.code === 0) {
					context?.$message.success(res.data.msg)
					getList()
				} else {
					context?.$message.error(res.data.msg)
				}
			})
		})
	}

	// 批量重试
	const retryBatchClick = () => {
		if (selRows.value.length === 0) {
			context?.$message.warning('请选择要重试的记录')
			return
		}
		const ids = selRows.value.map(row => row.id)
		ElMessageBox.confirm(`确定要批量重试选中的 ${ids.length} 条通知吗？`, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		}).then(() => {
			context?.$http({
				url: 'tongzhijilu/retryBatch',
				method: 'post',
				data: ids
			}).then(res => {
				if (res.data.code === 0) {
					context?.$message.success(res.data.msg)
					getList()
				} else {
					context?.$message.error(res.data.msg)
				}
			})
		})
	}

	// 统计
	const statisticsClick = () => {
		context?.$http({
			url: 'tongzhijilu/statistics',
			method: 'get'
		}).then(res => {
			if (res.data.code === 0) {
				Object.assign(statistics, res.data.data)
				statisticsVisible.value = true
			}
		})
	}

	// 获取类型名称
	const getTongzhileixingName = (type) => {
		switch (type) {
			case 1: return '预约成功通知'
			case 2: return '就诊前一天提醒'
			case 3: return '就诊当天提醒'
			case 4: return '就诊后随访提醒'
			default: return '未知类型'
		}
	}

	const getFasongzhuangtaiName = (status) => {
		switch (status) {
			case 0: return '待发送'
			case 1: return '发送成功'
			case 2: return '发送失败'
			default: return '未知状态'
		}
	}

	const getJieshouzhuangtaiName = (status) => {
		switch (status) {
			case 0: return '未接收'
			case 1: return '已接收'
			case 2: return '已读'
			default: return '未知状态'
		}
	}

	onMounted(() => {
		getList()
	})
</script>

<style scoped>
.statistics-card {
	text-align: center;
	padding: 20px;
}
.statistics-card.pending {
	background: #ecf5ff;
	border-left: 4px solid #409eff;
}
.statistics-card.success {
	background: #f0f9eb;
	border-left: 4px solid #67c23a;
}
.statistics-card.fail {
	background: #fef0f0;
	border-left: 4px solid #f56c6c;
}
.statistics-card.total {
	background: #f4f4f5;
	border-left: 4px solid #909399;
}
.statistics-number {
	font-size: 32px;
	font-weight: bold;
	margin-bottom: 10px;
}
.statistics-label {
	font-size: 14px;
	color: #666;
}
</style>
