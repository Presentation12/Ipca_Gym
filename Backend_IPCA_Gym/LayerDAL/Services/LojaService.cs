using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class LojaService
    {

        #region DEFAULT REQUESTS

        /// <summary>
        /// Leitura dos dados de todos os produtos da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de produtos se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Loja>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Loja";

            try
            {
                List<Loja> produtos = new List<Loja>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Loja produto = new Loja();

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

                            produtos.Add(produto);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return produtos;
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
        /// Leitura dos dados de um produto através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do produto a ser lido</param>
        /// <returns>Produto se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Loja> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Loja where id_produto = @id_produto";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_produto", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Loja targetLoja = new Loja();
                            targetLoja.id_produto = reader.GetInt32(0);
                            targetLoja.id_ginasio = reader.GetInt32(1);
                            targetLoja.nome = reader.GetString(2);
                            targetLoja.tipo_produto = reader.GetString(3);
                            targetLoja.preco = reader.GetDouble(4);
                            targetLoja.descricao = reader.GetString(5);
                            targetLoja.estado = reader.GetString(6);

                            if (!Convert.IsDBNull(reader["foto_produto"]))
                            {
                                targetLoja.foto_produto = reader.GetString(7);
                            }
                            else
                            {
                                targetLoja.foto_produto = null;
                            }

                            targetLoja.quantidade = reader.GetInt32(8);

                            reader.Close();
                            databaseConnection.Close();

                            return targetLoja;
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
        /// Inserção dos dados de um novo produto na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newProduto">Objeto com os dados do novo produto</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PostService(string sqlDataSource, Loja newProduto)
        {
            string query = @"
                            insert into dbo.Loja (id_ginasio, nome, tipo_produto, preco, descricao, estado, foto_produto, quantidade)
                            values (@id_ginasio, @nome, @tipo_produto, @preco, @descricao, @estado, @foto_produto, @quantidade)";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", newProduto.id_ginasio);
                        myCommand.Parameters.AddWithValue("nome", newProduto.nome);
                        myCommand.Parameters.AddWithValue("tipo_produto", newProduto.tipo_produto);
                        myCommand.Parameters.AddWithValue("preco", newProduto.preco);
                        myCommand.Parameters.AddWithValue("descricao", newProduto.descricao);
                        myCommand.Parameters.AddWithValue("estado", newProduto.estado);

                        if (!string.IsNullOrEmpty(newProduto.foto_produto)) myCommand.Parameters.AddWithValue("foto_produto", newProduto.foto_produto);
                        else myCommand.Parameters.AddWithValue("foto_produto", DBNull.Value);

                        myCommand.Parameters.AddWithValue("quantidade", newProduto.quantidade);

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
        /// Atualiza os dados de um produto através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="produto">Objeto com os novos dados do produto</param>
        /// <param name="targetID">ID do produto a ser atualizado</param>
        /// <returns>True se a atualização dos dados foi bem sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PatchService(string sqlDataSource, Loja produto, int targetID)
        {
            string query = @"
                            update dbo.Loja 
                            set id_ginasio = @id_ginasio, 
                            nome = @nome, 
                            tipo_produto = @tipo_produto,
                            preco = @preco,
                            descricao = @descricao,
                            estado = @estado,
                            foto_produto = @foto_produto,
                            quantidade = @quantidade
                            where id_produto = @id_produto";

            try
            {
                Loja produtoAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_produto", produto.id_produto != 0 ? produto.id_produto : produtoAtual.id_produto);
                        myCommand.Parameters.AddWithValue("id_ginasio", produto.id_ginasio != 0 ? produto.id_ginasio : produtoAtual.id_ginasio);
                        myCommand.Parameters.AddWithValue("nome", !string.IsNullOrEmpty(produto.nome) ? produto.nome : produtoAtual.nome);
                        myCommand.Parameters.AddWithValue("tipo_produto", !string.IsNullOrEmpty(produto.tipo_produto) ? produto.tipo_produto : produtoAtual.tipo_produto);
                        myCommand.Parameters.AddWithValue("preco", produto.preco != (double)0 ? produto.preco : produtoAtual.preco);
                        myCommand.Parameters.AddWithValue("descricao", !string.IsNullOrEmpty(produto.descricao) ? produto.descricao : produtoAtual.descricao);
                        myCommand.Parameters.AddWithValue("estado", !string.IsNullOrEmpty(produto.estado) ? produto.estado : produtoAtual.estado);
                        myCommand.Parameters.AddWithValue("foto_produto", !string.IsNullOrEmpty(produto.foto_produto) ? produto.foto_produto : produtoAtual.foto_produto);
                        myCommand.Parameters.AddWithValue("quantidade", produto.quantidade);

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
        /// Remoção de um produto da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID da produto a ser removido</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Loja 
                            where id_produto = @id_produto";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_produto", targetID);
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
        /// Leitura dos dados de todos os produtos de um ginásio através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do ginásio a ser lido</param>
        /// <returns>Lista de produto se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Loja>> GetAllByGinasioIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Loja where id_ginasio = @targetID";

            try
            {
                List<Loja> produtos = new List<Loja>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", targetID);
                        myCommand.Parameters.AddWithValue("targetID", targetID);
                        dataReader = myCommand.ExecuteReader();

                        while (dataReader.Read())
                        {
                            Loja produto = new Loja();

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

                            produtos.Add(produto);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return produtos;
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
        
        #endregion
    }
}
