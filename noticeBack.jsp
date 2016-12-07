<%@ page import="java.util.HashMap,net.joycool.wap.framework.*"%><%if (request.getSession().getAttribute("noticeBackLinkMap") == null) {
				//response.sendRedirect("backFailure.jsp?backTo="
				//		+ request.getParameter("backTo"));
				BaseAction.sendRedirect("/backFailure.jsp?backTo="
						+ request.getParameter("backTo"), response);
				return;
			}
			HashMap noticeBackLinkMap = (HashMap) request.getSession()
					.getAttribute("noticeBackLinkMap");
			String backTo = (String) noticeBackLinkMap.get("backTo");

			// only click one times notice back link
			request.getSession().removeAttribute("noticeBackLinkMap");

			response.sendRedirect((backTo));

		%>
