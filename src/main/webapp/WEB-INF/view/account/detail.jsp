<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<style>
.user--box {
	border: 1px solid black;
	padding: 10px;
}
</style>
<!-- todo main 영역으로 지정 예정 -->
<div class="col-sm-8 d-flex flex-column">
	<h2>계좌 상세 보기(인증)</h2>
	<h5>어서오세요 환영합니다.</h5>
	<!-- 부트스트랩 w3c에서 -->
	<div class="bg-light p-md-5 h-75">
		<div class="user--box">
			${principal.username}님 계좌<br> 계좌번호 : ${account.number}<br> 잔액 : ${account.formatBalance()}
		</div>
		<!-- 버튼을 누를 때마다 출금, 입금 내역 등 보여주기 -->
		<div>
			<br> <a href="/account/detail/${account.id}">전체</a>&nbsp;
			 <a href="/account/detail/${account.id}?type=deposit">입금</a>&nbsp;
			 <a href="/account/detail/${account.id}?type=withdraw">출금</a>&nbsp;
		</div>
		<br>
		<table class="table">
			<thead>
				<tr>
					<th>날짜</th>
					<th>보낸 이</th>
					<th>받은 이</th>
					<th>입출금 금액</th>
					<th>계좌 잔액</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="history" items="${historyList}">
					<tr>
						<th>${history.formatCreatedAt()}</th>
						<th>${history.sender}</th>
						<th>${history.receiver}</th>
						<th>${history.formatBalance()}</th>
						<th>${history.formatBalance()}</th>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<br>
</div>

<%@ include file="/WEB-INF/view/layout/footer.jsp"%>