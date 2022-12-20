using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;


namespace LayerDAL.Services
{
    public class PedidoLojaService
    {
        public static async Task<List<PedidoLoja>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Pedido_Loja";
            List<PedidoLoja> pedidosloja = new List<PedidoLoja>();

            try
            {
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
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<PedidoLoja> GetByIDService(string sqlDataSource, int targetID1, int targetID2)
        {
            string query = @"select * from dbo.Pedido_Loja where id_pedido = @id_pedido and id_produto = @id_produto";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_pedido", targetID1);
                        myCommand.Parameters.AddWithValue("id_produto", targetID2);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            PedidoLoja targetPedidoLoja = new PedidoLoja();
                            targetPedidoLoja.id_pedido = reader.GetInt32(0);
                            targetPedidoLoja.id_produto = reader.GetInt32(1);
                            targetPedidoLoja.quantidade = reader.GetInt32(2);

                            reader.Close();
                            databaseConnection.Close();

                            return targetPedidoLoja;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<bool> PostService(string sqlDataSource, PedidoLoja newPedidoLoja)
        {
            string query = @"
                            insert into dbo.Pedido_Loja (id_pedido, id_produto, quantidade)
                            values (@id_pedido, @id_produto, @quantidade)";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_pedido", newPedidoLoja.id_pedido);
                        myCommand.Parameters.AddWithValue("id_produto", newPedidoLoja.id_produto);
                        myCommand.Parameters.AddWithValue("quantidade", newPedidoLoja.quantidade);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                Console.Write(ex.ToString());
                return false;
            }
        }

        public static async Task<bool> PatchService(string sqlDataSource, PedidoLoja pedidoLoja, int targetID1, int targetID2)
        {
            string query = @"
                            update dbo.Pedido_Loja 
                            set quantidade = @quantidade
                            where id_pedido = @id_pedido and id_produto = @id_produto";

            try
            {
                PedidoLoja pedidoLojaAtual = await GetByIDService(sqlDataSource, targetID1, targetID2);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        if (pedidoLoja.id_pedido != null) myCommand.Parameters.AddWithValue("id_pedido", pedidoLoja.id_pedido);
                        else myCommand.Parameters.AddWithValue("id_pedido", pedidoLojaAtual.id_pedido);

                        if (pedidoLoja.id_produto != null) myCommand.Parameters.AddWithValue("id_produto", pedidoLoja.id_produto);
                        else myCommand.Parameters.AddWithValue("id_produto", pedidoLojaAtual.id_produto);

                        if (pedidoLoja.quantidade != null) myCommand.Parameters.AddWithValue("quantidade", pedidoLoja.quantidade);
                        else myCommand.Parameters.AddWithValue("quantidade", pedidoLojaAtual.quantidade);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        public static async Task<bool> DeleteService(string sqlDataSource, int targetID1, int targetID2)
        {
            string query = @"
                            delete from dbo.Pedido_Loja 
                            where id_pedido = @id_pedido and id_produto = @id_produto";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_pedido", targetID1);
                        myCommand.Parameters.AddWithValue("id_produto", targetID2);
                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }
    }
}
