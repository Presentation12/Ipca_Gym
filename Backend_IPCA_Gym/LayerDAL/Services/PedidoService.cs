using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class PedidoService
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Leitura dos dados de todos os pedidos da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de pedidos se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Pedido>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Pedido";

            try
            {
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

        /// <summary>
        /// Leitura dos dados de um pedido através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do pedido a ser lido</param>
        /// <returns>Pedido se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Pedido> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Pedido where id_pedido = @id_pedido";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_pedido", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Pedido targetPedido = new Pedido();
                            targetPedido.id_pedido = reader.GetInt32(0);
                            targetPedido.id_cliente = reader.GetInt32(1);
                            targetPedido.estado = reader.GetString(2);
                            targetPedido.data_pedido = reader.GetDateTime(3);

                            reader.Close();
                            databaseConnection.Close();

                            return targetPedido;
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

        /// <summary>
        /// Inserção dos dados de um novo pedido na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newPedido">Objeto com os dados do novo pedido</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PostService(string sqlDataSource, Pedido newPedido)
        {
            string query = @"
                            insert into dbo.Pedido (id_cliente, data_pedido, estado)
                            values (@id_cliente, @data_pedido, @estado)";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_cliente", newPedido.id_cliente);
                        myCommand.Parameters.AddWithValue("data_pedido", Convert.ToDateTime(newPedido.data_pedido));
                        myCommand.Parameters.AddWithValue("estado", newPedido.estado);

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

        /// <summary>
        /// Atualiza os dados de um pedido através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="pedido">Objeto com os novos dados da pedido</param>
        /// <param name="targetID">ID do pedido a ser atualizado</param>
        /// <returns>True se a atualização dos dados foi bem-sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PatchService(string sqlDataSource, Pedido pedido, int targetID)
        {
            string query = @"
                            update dbo.Pedido 
                            set id_cliente = @id_cliente, 
                            data_pedido = @data_pedido,
                            estado = @estado
                            where id_pedido = @id_pedido";

            try
            {
                Pedido pedidoAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_pedido", pedido.id_pedido != 0 ? pedido.id_pedido : pedidoAtual.id_pedido);
                        myCommand.Parameters.AddWithValue("id_cliente", pedido.id_cliente != 0 ? pedido.id_cliente : pedidoAtual.id_cliente);
                        myCommand.Parameters.AddWithValue("data_pedido", !pedido.data_pedido.Equals(DateTime.MinValue) ? pedido.data_pedido : pedidoAtual.data_pedido);
                        myCommand.Parameters.AddWithValue("estado", !string.IsNullOrEmpty(pedido.estado) ? pedido.estado : pedidoAtual.estado);

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

        /// <summary>
        /// Remoção de um pedido da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID do pedido a ser removido</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Pedido 
                            where id_pedido = @id_pedido";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_pedido", targetID);
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

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Leitura dos dados de todos os pedidos de um cliente através do seu id de cliente na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do cliente ao qual pertencem os pedidos a ser lido</param>
        /// <returns>Pedidos do cliente se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Pedido>> GetAllByClienteIDService(string sqlDataSource, int targetID)

        {
            string query = @"select * from dbo.Pedido where id_cliente = @targetID";

            try
            {
                List<Pedido> pedidos = new List<Pedido>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_cliente", targetID);
                        myCommand.Parameters.AddWithValue("targetID", targetID);
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

        /// <summary>
        /// Leitura dos dados de todos os pedidos de um cliente da base de dados ligados aos prosutos (join)
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="TargetID">ID do cliente ao qual pertencem os pedidos a ser lido</param>
        /// <returns>Lista de pedidos se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Object>> GetAllConnectionClientService(string sqlDataSource, int TargetID)
        {
            string query = @"SELECT * FROM Pedido
                            INNER JOIN Pedido_Loja ON Pedido.id_pedido = Pedido_Loja.id_pedido
                            INNER JOIN Loja ON Loja.id_produto = Pedido_Loja.id_produto
                            WHERE Pedido.id_cliente = @id_cliente";

            try
            {
                List<Object> join = new List<Object>();

                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("@id_cliente", TargetID);
                        dataReader = myCommand.ExecuteReader();

                        // ALTERNATIVA TER UM MODEL OU OBJETO PARA ARMAZENAR TODOS

                        while (dataReader.Read())
                        {
                            Pedido pedido = new Pedido();
                            PedidoLoja pedidoLoja = new PedidoLoja();
                            Loja produto = new Loja();

                            pedido.id_pedido = Convert.ToInt32(dataReader["id_pedido"]);
                            pedido.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                            pedido.data_pedido = Convert.ToDateTime(dataReader["data_pedido"]);
                            pedido.estado = dataReader["estado"].ToString();

                            join.Add(pedido);
                             

                            pedidoLoja.id_pedido = Convert.ToInt32(dataReader["id_pedido"]);
                            pedidoLoja.quantidade = Convert.ToInt32(dataReader["quantidade"]);
                            pedidoLoja.id_produto = Convert.ToInt32(dataReader["id_produto"]);

                            join.Add(pedidoLoja);

                            produto.id_produto = Convert.ToInt32(dataReader["id_produto"]);
                            produto.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                            produto.nome = dataReader["nome"].ToString();
                            produto.descricao = dataReader["descricao"].ToString();
                            produto.preco = Convert.ToDouble(dataReader["preco"]);
                            produto.tipo_produto = dataReader["tipo_produto"].ToString();
                            produto.estado = dataReader["estado"].ToString();
                            if (!Convert.IsDBNull(dataReader["foto_produto"]))
                            {
                                produto.foto_produto = dataReader["foto_produto"].ToString();
                            }
                            else
                            {
                                produto.foto_produto = null;
                            }
                            produto.quantidade = Convert.ToInt32(dataReader["quantidade"]);

                            join.Add(produto);

                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return join;
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

        #endregion
    }
}
