<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true"/>"><span></span>
			</a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<sec:authorize access="isAuthenticated()">
				<petclinic:menuItem active="${name eq 'boards'}" url="/play"
					title="Board List">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Jugar</span>
				</petclinic:menuItem>
				</sec:authorize>


				<sec:authorize access="hasAuthority('admin')">
				<petclinic:menuItem active="${name eq 'playersA'}" url="/admin"
					title="admin">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Admin</span>
				</petclinic:menuItem>
				</sec:authorize>


				<sec:authorize access="isAuthenticated()">
				<petclinic:menuItem active="${name eq 'Foro'}" url="/forum"
					title="Foro">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Foro</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="isAuthenticated()">
				<petclinic:menuItem active="${name eq 'friends'}" url="/friendList"
					title="Friend List">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Amigos</span>
				</petclinic:menuItem>
				</sec:authorize>

 				<sec:authorize access="isAuthenticated()">
 				
				<petclinic:menuItem active="${name eq 'Estadistics'}" url="/globalEstadistics"
					title="Estadistics">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Estadísticas</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				
				<sec:authorize access="isAuthenticated()">
				<li >
                     <form class="form-inline" action="/findPlayer" method="get" style="margin:1.5em">
                       <div class="input-group">
                         <input size="13px" type="text" class="form-control" name="username" placeholder="Busca Jugadores" aria-label="Username" aria-describedby="basic-addon1">
                       </div>
                     </form>
                </li>
                </sec:authorize>
                
              
                
                






			</ul>
			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
					<li><a href="<c:url value="/users/new" />">Register</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											
											
											<p class="text">
												   <spring:url value="/details/{playerUsername}" var="playerUrl">
												   <sec:authentication var="user" property="principal" />
                        						   <spring:param name="playerUsername" value="${user.username}"/>
                   								   </spring:url>
                    							   <a href="${fn:escapeXml(playerUrl)}" class="btn btn-primary btn-block btn-sm">Profile Data</a>
											</p>
											
											
											<p class="text">
                                                   <a href="<c:url value="/player/edit" />" class="btn btn-primary btn-block btn-sm">Change profile</a>
                                            </p>
											<p class="text">
												<a href="<c:url value="/logout" />" class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>

										</div>

									</div>
								</div>
							</li>
							<li class="divider"></li>
<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change Profile</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
