using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class ClassificacaoService
    {
        public static async Task<List<Classificacao>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Classificacao";
            List<Classificacao> classificacoes = new List<Classificacao>();

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
                            Classificacao classificacao = new Classificacao();

                            classificacao.id_avaliacao = Convert.ToInt32(dataReader["id_avaliacao"]);
                            classificacao.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                            classificacao.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                            classificacao.avaliacao = Convert.ToInt32(dataReader["avaliacao"]);
                            classificacao.comentario = dataReader["comentario"].ToString();
                            classificacao.data_avaliacao = Convert.ToDateTime(dataReader["data_avaliacao"]);

                            classificacoes.Add(classificacao);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }
                return classificacoes;
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

        public static async Task<Classificacao> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Classificacao where id_avaliacao = @id_avaliacao";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_avaliacao", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Classificacao targetClassificao = new Classificacao();
                            targetClassificao.id_avaliacao = reader.GetInt32(0);
                            targetClassificao.id_ginasio = reader.GetInt32(1);
                            targetClassificao.id_cliente = reader.GetInt32(2);
                            targetClassificao.avaliacao = reader.GetInt32(3);
                            targetClassificao.comentario = reader.GetString(4);
                            targetClassificao.data_avaliacao = reader.GetDateTime(5);

                            reader.Close();
                            databaseConnection.Close();

                            return targetClassificao;
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

        public static async Task<bool> PostService(string sqlDataSource, Classificacao newClassificacao)
        {
            string query = @"
                            insert into dbo.Classificacao (id_ginasio, id_cliente, avaliacao, comentario, data_avaliacao)
                            values (@id_ginasio, @id_cliente, @avaliacao, @comentario, @data_avaliacao)";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", newClassificacao.id_ginasio);
                        myCommand.Parameters.AddWithValue("id_cliente", newClassificacao.id_cliente);
                        myCommand.Parameters.AddWithValue("avaliacao", newClassificacao.avaliacao);
                        myCommand.Parameters.AddWithValue("comentario", newClassificacao.comentario);
                        myCommand.Parameters.AddWithValue("data_avaliacao", Convert.ToDateTime(newClassificacao.data_avaliacao));
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

        public static async Task<bool> PatchService(string sqlDataSource, Classificacao classificacao, int targetID)
        {
            string query = @"
                            update dbo.Classificacao 
                            set id_ginasio = @id_ginasio, 
                            id_cliente = @id_cliente, 
                            avaliacao = @avaliacao, 
                            comentario = @comentario,
                            data_avaliacao = @data_avaliacao
                            where id_avaliacao = @id_avaliacao";

            try
            {
                Classificacao classificacaoAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_avaliacao", classificacao.id_avaliacao != 0 ? classificacao.id_avaliacao : classificacaoAtual.id_avaliacao);
                        myCommand.Parameters.AddWithValue("id_ginasio", classificacao.id_ginasio != 0 ? classificacao.id_ginasio : classificacaoAtual.id_ginasio);
                        myCommand.Parameters.AddWithValue("id_cliente", classificacao.id_cliente != 0 ? classificacao.id_cliente : classificacaoAtual.id_cliente);
                        myCommand.Parameters.AddWithValue("avaliacao", classificacao.avaliacao != 0 ? classificacao.avaliacao : classificacaoAtual.avaliacao);
                        myCommand.Parameters.AddWithValue("comentario", !string.IsNullOrEmpty(classificacao.comentario) ? classificacao.comentario : classificacaoAtual.comentario);
                        myCommand.Parameters.AddWithValue("data_avaliacao", classificacao.data_avaliacao.Equals(DateTime.MinValue) ? Convert.ToDateTime(classificacao.data_avaliacao) : Convert.ToDateTime(classificacaoAtual.data_avaliacao));
                    
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

        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Classificacao 
                            where id_avaliacao = @id_avaliacao";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_avaliacao", targetID);
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
