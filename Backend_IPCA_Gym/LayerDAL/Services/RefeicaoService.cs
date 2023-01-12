using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;


namespace LayerDAL.Services
{
    public class RefeicaoService
    {
        /// <summary>
        /// Leitura dos dados de todas as refeições da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de refeições se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Refeicao>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Refeicao";

            try
            {
                List<Refeicao> refeicoes = new List<Refeicao>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Refeicao refeicao = new Refeicao();

                            refeicao.id_refeicao = Convert.ToInt32(dataReader["id_refeicao"]);
                            refeicao.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                            refeicao.descricao = dataReader["descricao"].ToString();
                            refeicao.hora = (TimeSpan)dataReader["hora"];
                            if (!Convert.IsDBNull(dataReader["foto_refeicao"]))
                            {
                                refeicao.foto_refeicao = dataReader["foto_refeicao"].ToString();
                            }
                            else
                            {
                                refeicao.foto_refeicao = null;
                            }


                            refeicoes.Add(refeicao);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return refeicoes;
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
        /// Leitura dos dados de uma refeição através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID da refeição a ser lida</param>
        /// <returns>Refeição se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Refeicao> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Refeicao where id_refeicao = @id_refeicao";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_refeicao", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Refeicao targetRefeicao = new Refeicao();
                            targetRefeicao.id_refeicao = reader.GetInt32(0);
                            targetRefeicao.id_plano_nutricional = reader.GetInt32(1);
                            targetRefeicao.descricao = reader.GetString(2);
                            targetRefeicao.hora = reader.GetTimeSpan(3);

                            if (!Convert.IsDBNull(reader["foto_refeicao"]))
                            {
                                targetRefeicao.foto_refeicao = reader.GetString(4);
                            }
                            else
                            {
                                targetRefeicao.foto_refeicao = null;
                            }

                            reader.Close();
                            databaseConnection.Close();

                            return targetRefeicao;
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
        /// Inserção dos dados de uma nova refeição na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newRefeicao">Objeto com os dados da nova refeição</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PostService(string sqlDataSource, Refeicao newRefeicao)
        {
            string query = @"
                            insert into dbo.Refeicao (id_plano_nutricional, descricao, hora, foto_refeicao)
                            values (@id_plano_nutricional, @descricao, @hora, @foto_refeicao)";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_plano_nutricional", newRefeicao.id_plano_nutricional);
                        myCommand.Parameters.AddWithValue("descricao", newRefeicao.descricao);
                        myCommand.Parameters.AddWithValue("hora", newRefeicao.hora);

                        if (!string.IsNullOrEmpty(newRefeicao.foto_refeicao)) myCommand.Parameters.AddWithValue("foto_refeicao", newRefeicao.foto_refeicao);
                        else myCommand.Parameters.AddWithValue("foto_refeicao", DBNull.Value);

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
        /// Atualiza os dados de uma refeição através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="refeição">Objeto com os novos dados da refeição</param>
        /// <param name="targetID">ID da refeição a ser atualizada</param>
        /// <returns>True se a atualização dos dados foi bem sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PatchService(string sqlDataSource, Refeicao refeicao, int targetID)
        {
            string query = @"
                            update dbo.Refeicao 
                            set id_plano_nutricional = @id_plano_nutricional, 
                            descricao = @descricao,
                            hora = @hora,
                            foto_refeicao = @foto_refeicao
                            where id_refeicao = @id_refeicao";

            try
            {
                Refeicao refeicaoAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_refeicao", refeicao.id_refeicao != 0 ? refeicao.id_refeicao : refeicaoAtual.id_refeicao);
                        myCommand.Parameters.AddWithValue("id_plano_nutricional", refeicao.id_plano_nutricional != 0 ? refeicao.id_plano_nutricional : refeicaoAtual.id_plano_nutricional);
                        myCommand.Parameters.AddWithValue("descricao", !string.IsNullOrEmpty(refeicao.descricao) ? refeicao.descricao : refeicaoAtual.descricao);
                        myCommand.Parameters.AddWithValue("hora", refeicao.hora != TimeSpan.Zero ? refeicao.hora : refeicaoAtual.hora);

                        if (refeicao.foto_refeicao != null)
                            myCommand.Parameters.AddWithValue("foto_refeicao", refeicao.foto_refeicao);
                        else
                            myCommand.Parameters.AddWithValue("foto_refeicao", refeicaoAtual.foto_refeicao != null ? refeicaoAtual.foto_refeicao : DBNull.Value);


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
        /// Remoção de uma refeição da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID da refeição a ser removida</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Refeicao 
                            where id_refeicao = @id_refeicao";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_refeicao", targetID);
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

        /// <summary>
        /// Retornar uma lista de exercicios de um plano nutricional em especifico
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do plano de nutricao ao qual pertencem as refeicoes a ser lidas</param>
        /// <returns>Plano nutricional se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Refeicao>> GetAllByPlanoIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Refeicao where id_plano_nutricional = @targetID";

            try
            {
                List<Refeicao> plano = new List<Refeicao>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("targetID", targetID);
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Refeicao refeicao = new Refeicao();

                            refeicao.id_refeicao = Convert.ToInt32(dataReader["id_refeicao"]);
                            refeicao.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                            refeicao.descricao = dataReader["descricao"].ToString();
                            refeicao.hora = (TimeSpan)dataReader["hora"];
                            if (!Convert.IsDBNull(dataReader["foto_refeicao"]))
                            {
                                refeicao.foto_refeicao = dataReader["foto_refeicao"].ToString();
                            }
                            else
                            {
                                refeicao.foto_refeicao = null;
                            }

                            plano.Add(refeicao);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return plano;
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
    }
}
