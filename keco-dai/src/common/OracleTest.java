package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleTest {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		Connection conn = null; // DB연결된 상태(세션)을 담은 객체
		PreparedStatement pstm = null; // SQL 문을 나타내는 객체
		ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
		
		try{
			
			// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
            // 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
            String quary = "SELECT * FROM INFOREPLY";
            
            conn = DBConnection.getOraConnection_local();
            pstm = conn.prepareStatement(quary, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pstm.executeQuery();
            
          //전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
			rs.last();
			
			int rowCount = rs.getRow();
			
			//전체 레코드 개수만큼의 배열
			String[] rno = new String[rowCount];
			String[] bno = new String[rowCount];
			String[] replyText = new String[rowCount];
			String[] replyer = new String[rowCount];
			String[] regdate = new String[rowCount];
			
			//다시 처음부터 조회해야 하므로 커서는 초기화
			rs.beforeFirst();
			
			int i=0;
			
			while (rs.next()) {
				
				rno[i] = rs.getString(1);
				bno[i] = rs.getString(2);
				replyText[i] = rs.getString(3);
				replyer[i] = rs.getString(4);
				regdate[i] = rs.getString(5);
				
				System.out.println("rno::"+rno[i]+"::bno::"+bno[i]+"::replyText::"+replyText[i]+"::replyer::"+replyer[i]+"::regdate::"+regdate[i]);
				 i++;
				
			}
			
			
		} catch (SQLException sqle) {
            System.out.println("SELECT문에서 예외 발생");
            sqle.printStackTrace();
            
        }finally{
            // DB 연결을 종료한다.
            try{
                if ( rs != null ){rs.close();}   
                if ( pstm != null ){pstm.close();}   
                if ( conn != null ){conn.close(); }
            }catch(Exception e){
                throw new RuntimeException(e.getMessage());
            }
            
        }



	}

}
