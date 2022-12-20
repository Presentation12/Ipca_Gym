using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL
{
    public class PedidoService
    {
        public static async Task<List<Pedido>> GetPedidosService(string sqlDataSource)
        {
            string query = @"select * from dbo.Pedido";
            List<Pedido> pedidos = new List<Pedido>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        Pedido pedido = new Pedido();

                        pedido.id_pedido = Convert.ToInt32(dataReader["id_pedido"]);
                        pedido.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                        pedido.data_pedido = Convert.ToDateTime(dataReader["data_pedido"]);
                        pedido.estado = dataReader["estado"].ToString();

                        pedidos.Add(pedido);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return pedidos;
        }
    }
}
