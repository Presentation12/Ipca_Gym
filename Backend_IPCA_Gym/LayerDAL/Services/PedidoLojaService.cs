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
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return null;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
                return null;
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Erro de leitura dos dados: " + ex.Message);
                return null;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return null;
            }
            catch (IndexOutOfRangeException ex)
            {
                Console.WriteLine("Erro de acesso a uma coluna da base de dados: " + ex.Message);
                return null;
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
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return null;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
                return null;
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Erro de leitura dos dados: " + ex.Message);

                return null;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return null;
            }
            catch (IndexOutOfRangeException ex)
            {
                Console.WriteLine("Erro de acesso a uma coluna da base de dados: " + ex.Message);
                return null;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return null;
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
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
                return false;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
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
                        myCommand.Parameters.AddWithValue("id_pedido", pedidoLoja.id_pedido != 0 ? pedidoLoja.id_pedido : pedidoLojaAtual.id_pedido);
                        myCommand.Parameters.AddWithValue("id_produto", pedidoLoja.id_produto != 0 ? pedidoLoja.id_produto : pedidoLojaAtual.id_produto);
                        myCommand.Parameters.AddWithValue("quantidade", pedidoLoja.quantidade);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
                return false;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
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
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }
    }
}
