package org.test.toolkit.database;


public class DbClient {
	public static void main(String[] args) {/*
		boolean flag = false;

 		PreparedStatement pstmt = null;
 		ResultSet rs = null;
 		String sql = null;
 		Connection con = null;
 		con = null;

 		sql = "SELECT u_name,u_password FROM nwvideo.n_user WHERE u_name=? and u_password=?";
		try {
 			pstmt = con.prepareStatement(sql);

			System.out.println("操作对象已被实例化");

 			pstmt.setString(1, "limeng");
			pstmt.setString(2, "limeng");

			System.out.println("获得username,password");

			// 查询记录
			rs = pstmt.executeQuery();
			System.out.println("执行查询完毕");
 			if (rs.next()) {
 				flag = true;

				System.out.println("用户合法");
			}
			// 依次关闭

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
 			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/
	}
}