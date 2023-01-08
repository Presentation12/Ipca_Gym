using LayerBOL.Models;
using System;
using System.Data;
using System.Data.SqlClient;


namespace LayerDAL.Services
{
    public class ExercicioService
    {
        /// <summary>
        /// Leitura dos dados de todos os exercicios da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de exercicios se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Exercicio>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Exercicio";
            List<Exercicio> exercicios = new List<Exercicio>();

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
                            Exercicio exercicio = new Exercicio();

                            exercicio.id_exercicio = Convert.ToInt32(dataReader["id_exercicio"]);
                            exercicio.id_plano_treino = Convert.ToInt32(dataReader["id_plano_treino"]);
                            exercicio.nome = dataReader["nome"].ToString();
                            exercicio.descricao = dataReader["descricao"].ToString();
                            exercicio.tipo = dataReader["tipo"].ToString();
                            if (!Convert.IsDBNull(dataReader["series"]))
                            {
                                exercicio.series = Convert.ToInt32(dataReader["series"]);
                            }
                            else
                            {
                                exercicio.series = null;
                            }
                            if (!Convert.IsDBNull(dataReader["tempo"]))
                            {
                                exercicio.tempo = (TimeSpan?)dataReader["tempo"];
                            }
                            else
                            {
                                exercicio.tempo = null;
                            }
                            if (!Convert.IsDBNull(dataReader["repeticoes"]))
                            {
                                exercicio.repeticoes = Convert.ToInt32(dataReader["repeticoes"]);
                            }
                            else
                            {
                                exercicio.repeticoes = null;
                            }
                            if (!Convert.IsDBNull(dataReader["foto_exercicio"]))
                            {
                                exercicio.foto_exercicio = dataReader["foto_exercicio"].ToString();
                            }
                            else
                            {
                                exercicio.foto_exercicio = null;
                            }


                            exercicios.Add(exercicio);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }
                return exercicios;
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
                Console.Write(ex.ToString());
                return null;
            }
        }

        /// <summary>
        /// Leitura dos dados de um exercicio através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do exercicio a ser lido</param>
        /// <returns>Exercicio se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Exercicio> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"
                            select * from dbo.Exercicio where id_exercicio = @id_exercicio";
            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_exercicio", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Exercicio targetExercicio = new Exercicio();
                            targetExercicio.id_exercicio = reader.GetInt32(0);
                            targetExercicio.id_plano_treino = reader.GetInt32(1);
                            targetExercicio.nome = reader.GetString(2);
                            targetExercicio.descricao = reader.GetString(3);
                            targetExercicio.tipo = reader.GetString(4);

                            if (!Convert.IsDBNull(reader["series"]))
                            {
                                targetExercicio.series = reader.GetInt32(5);
                            }
                            else
                            {
                                targetExercicio.series = null;
                            }

                            if (!Convert.IsDBNull(reader["tempo"]))
                            {
                                targetExercicio.tempo = reader.GetTimeSpan(6);
                            }
                            else
                            {
                                targetExercicio.tempo = new TimeSpan(0,0,0);
                            }

                            if (!Convert.IsDBNull(reader["repeticoes"]))
                            {
                                targetExercicio.repeticoes = reader.GetInt32(7);
                            }
                            else
                            {
                                targetExercicio.repeticoes = null;
                            }

                            if (!Convert.IsDBNull(reader["foto_exercicio"]))
                            {
                                targetExercicio.foto_exercicio = reader.GetString(8);
                            }
                            else
                            {
                                targetExercicio.foto_exercicio = null;
                            }

                            reader.Close();
                            databaseConnection.Close();

                            return targetExercicio;
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
        /// Inserção dos dados de um novo exercicio na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newExercicio">Objeto com os dados do novo exercicio</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PostService(string sqlDataSource, Exercicio newExercicio)
        {
            string query = @"
                            insert into dbo.Exercicio (id_plano_treino, nome, descricao, tipo, series, tempo, repeticoes, foto_exercicio)
                            values (@id_plano_treino, @nome, @descricao, @tipo, @series, @tempo, @repeticoes, @foto_exercicio)";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_plano_treino", newExercicio.id_plano_treino);
                        myCommand.Parameters.AddWithValue("nome", newExercicio.nome);
                        myCommand.Parameters.AddWithValue("descricao", newExercicio.descricao);
                        myCommand.Parameters.AddWithValue("tipo", newExercicio.tipo);

                        if (newExercicio.series != null) myCommand.Parameters.AddWithValue("series", newExercicio.series);
                        else myCommand.Parameters.AddWithValue("series", DBNull.Value);

                        if (newExercicio.tempo != null) myCommand.Parameters.AddWithValue("tempo", newExercicio.tempo);
                        else myCommand.Parameters.AddWithValue("tempo", DBNull.Value);

                        if (newExercicio.repeticoes != null) myCommand.Parameters.AddWithValue("repeticoes", newExercicio.repeticoes);
                        else myCommand.Parameters.AddWithValue("repeticoes", DBNull.Value);

                        if (!string.IsNullOrEmpty(newExercicio.foto_exercicio)) myCommand.Parameters.AddWithValue("foto_exercicio", newExercicio.foto_exercicio);
                        else myCommand.Parameters.AddWithValue("foto_exercicio", DBNull.Value);

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
        /// Atualiza os dados de um exercicio através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="exercicio">Objeto com os novos dados da exercicio</param>
        /// <param name="targetID">ID do exercicio a ser atualizado</param>
        /// <returns>True se a atualização dos dados foi bem-sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PatchService(string sqlDataSource, Exercicio exercicio, int targetID)
        {
            string query = @"
                            update dbo.Exercicio 
                            set id_plano_treino = @id_plano_treino, 
                            nome = @nome, 
                            descricao = @descricao,
                            tipo = @tipo,
                            series = @series,
                            tempo = @tempo,
                            repeticoes = @repeticoes,
                            foto_exercicio = @foto_exercicio
                            where id_exercicio = @id_exercicio";

            try
            {
                Exercicio exercicioAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_exercicio", exercicio.id_exercicio != 0 ? exercicio.id_exercicio : exercicioAtual.id_exercicio);
                        myCommand.Parameters.AddWithValue("id_plano_treino", exercicio.id_plano_treino != 0 ? exercicio.id_plano_treino : exercicioAtual.id_plano_treino);
                        myCommand.Parameters.AddWithValue("nome", !string.IsNullOrEmpty(exercicio.nome) ? exercicio.nome : exercicioAtual.nome);
                        myCommand.Parameters.AddWithValue("descricao", !string.IsNullOrEmpty(exercicio.descricao) ? exercicio.descricao : exercicioAtual.descricao);
                        myCommand.Parameters.AddWithValue("tipo", !string.IsNullOrEmpty(exercicio.tipo) ? exercicio.tipo : exercicioAtual.tipo);
                        myCommand.Parameters.AddWithValue("series", exercicio.series.HasValue ? exercicio.series : DBNull.Value);
                        myCommand.Parameters.AddWithValue("tempo", exercicio.tempo.HasValue ? exercicio.tempo : DBNull.Value);
                        myCommand.Parameters.AddWithValue("repeticoes", exercicio.repeticoes.HasValue ? exercicio.repeticoes : DBNull.Value);
                        myCommand.Parameters.AddWithValue("foto_exercicio", !string.IsNullOrEmpty(exercicio.foto_exercicio) ? exercicio.foto_exercicio : DBNull.Value);

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
        /// Remoção de um exercicio da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID do exercicio a ser removido</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Exercicio 
                            where id_exercicio = @id_exercicio";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_exercicio", targetID);
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
        /// Retornar uma lista de exercicios de um plano de treino em especifico
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do plano de treino ao qual pertencem os exercicios a ser lidas</param>
        /// <returns>Plano de Treino se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Exercicio>> GetAllByPlanoIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Exercicio where id_plano_treino = @targetID";

            try
            {
                List<Exercicio> plano = new List<Exercicio>();
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
                            Exercicio exercicio = new Exercicio();
                            
                            exercicio.id_exercicio = Convert.ToInt32(dataReader["id_exercicio"]);
                            exercicio.id_plano_treino = Convert.ToInt32(dataReader["id_plano_treino"]);
                            exercicio.nome = dataReader["nome"].ToString();
                            exercicio.descricao = dataReader["descricao"].ToString();
                            exercicio.tipo = dataReader["tipo"].ToString();
                            if (!Convert.IsDBNull(dataReader["series"]))
                            {
                                exercicio.series = Convert.ToInt32(dataReader["series"]);
                            }
                            else
                            {
                                exercicio.series = null;
                            }
                            if (!Convert.IsDBNull(dataReader["tempo"]))
                            {
                                exercicio.tempo = (TimeSpan?)dataReader["tempo"];
                            }
                            else
                            {
                                exercicio.tempo = null;
                            }
                            if (!Convert.IsDBNull(dataReader["repeticoes"]))
                            {
                                exercicio.repeticoes = Convert.ToInt32(dataReader["repeticoes"]);
                            }
                            else
                            {
                                exercicio.repeticoes = null;
                            }
                            if (!Convert.IsDBNull(dataReader["foto_exercicio"]))
                            {
                                exercicio.foto_exercicio = dataReader["foto_exercicio"].ToString();
                            }
                            else
                            {
                                exercicio.foto_exercicio = null;
                            }

                            plano.Add(exercicio);
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
