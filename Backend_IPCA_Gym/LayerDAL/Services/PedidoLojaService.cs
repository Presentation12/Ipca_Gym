using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;


namespace LayerDAL.Services
{
    public class PedidoLojaService
    {
        public static async Task<List<PedidoLoja>> GetPedidoLojasService(string sqlDataSource)
        {
            string query = @"select * from dbo.Pedido_Loja";
            List<PedidoLoja> pedidosloja = new List<PedidoLoja>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        PedidoLoja pedidoloja = new PedidoLoja();

                        pedidoloja.id_pedido = Convert.ToInt32(dataReader["id_pedido"]);
                        pedidoloja.id_produto = Convert.ToInt32(dataReader["id_produto"]);
                        pedidoloja.quantidade = Convert.ToInt32(dataReader["quantidade"]);

                        pedidosloja.Add(pedidoloja);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return pedidosloja;
        }
    }
}
