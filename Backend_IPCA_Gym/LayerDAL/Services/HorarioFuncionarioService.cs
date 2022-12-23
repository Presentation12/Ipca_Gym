using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class HorarioFuncionarioService
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Leitura dos dados de todos os horários (diário) de funcionarios da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de horários (diário) de funcionarios se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<HorarioFuncionario>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Horario_Funcionario";

            try
            {
                List<HorarioFuncionario> horarios = new List<HorarioFuncionario>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            HorarioFuncionario dia = new HorarioFuncionario();

                            dia.id_funcionario_horario = Convert.ToInt32(dataReader["id_funcionario_horario"]);
                            dia.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                            dia.hora_entrada = (TimeSpan)dataReader["hora_entrada"];
                            dia.hora_saida = (TimeSpan)dataReader["hora_saida"];
                            dia.dia_semana = dataReader["dia_semana"].ToString();

                            horarios.Add(dia);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }
                return horarios;
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
        /// Leitura dos dados de um horário (diário) de um funcionário através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do horário (diário) de um funcionário a ser lido</param>
        /// <returns>Horario (diário) de um funcionario se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<HorarioFuncionario> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Horario_Funcionario where id_funcionario_horario = @id_funcionario_horario";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_funcionario_horario", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            HorarioFuncionario targetHorarioFuncionario = new HorarioFuncionario();
                            targetHorarioFuncionario.id_funcionario_horario = reader.GetInt32(0);
                            targetHorarioFuncionario.id_funcionario = reader.GetInt32(1);
                            targetHorarioFuncionario.hora_entrada = reader.GetTimeSpan(2);
                            targetHorarioFuncionario.hora_saida = reader.GetTimeSpan(3);
                            targetHorarioFuncionario.dia_semana = reader.GetString(4);

                            reader.Close();
                            databaseConnection.Close();

                            return targetHorarioFuncionario;
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
        /// Inserção dos dados de um novo horário (diário) de um funcionário na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newHorarioFuncionario">Objeto com os dados do novo horário (diário) de um funcionário</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PostService(string sqlDataSource, HorarioFuncionario newHorarioFuncionario)
        {
            string query = @"
                            insert into dbo.Horario_Funcionario (id_funcionario, hora_entrada, hora_saida, dia_semana)
                            values (@id_funcionario, @hora_entrada, @hora_saida, @dia_semana)";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_funcionario", newHorarioFuncionario.id_funcionario);
                        myCommand.Parameters.AddWithValue("hora_entrada", newHorarioFuncionario.hora_entrada);
                        myCommand.Parameters.AddWithValue("hora_saida", newHorarioFuncionario.hora_saida);
                        myCommand.Parameters.AddWithValue("dia_semana", newHorarioFuncionario.dia_semana);

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
        /// Atualiza os dados de um horário (diário) de um funcionário através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="horarioFuncionario">Objeto com os novos dados da horário (diário) de um funcionário</param>
        /// <param name="targetID">ID da horário (diário) de um funcionário a ser atualizada</param>
        /// <returns>True se a atualização dos dados foi bem sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PatchService(string sqlDataSource, HorarioFuncionario horarioFuncionario, int targetID)
        {
            string query = @"
                            update dbo.Horario_Funcionario 
                            set id_funcionario = @id_funcionario, 
                            hora_entrada = @hora_entrada, 
                            hora_saida = @hora_saida,
                            dia_semana = @dia_semana
                            where id_funcionario_horario = @id_funcionario_horario";

            try
            {
                HorarioFuncionario horarioFuncionarioAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_funcionario_horario", horarioFuncionario.id_funcionario_horario != 0 ? horarioFuncionario.id_funcionario_horario : horarioFuncionarioAtual.id_funcionario_horario);
                        myCommand.Parameters.AddWithValue("id_funcionario", horarioFuncionario.id_funcionario != 0 ? horarioFuncionario.id_funcionario : horarioFuncionarioAtual.id_funcionario);
                        myCommand.Parameters.AddWithValue("hora_entrada", horarioFuncionario.hora_entrada != TimeSpan.Zero ? horarioFuncionario.hora_entrada : horarioFuncionarioAtual.hora_entrada);
                        myCommand.Parameters.AddWithValue("hora_saida", horarioFuncionario.hora_saida != TimeSpan.Zero ? horarioFuncionario.hora_saida : horarioFuncionarioAtual.hora_saida);
                        myCommand.Parameters.AddWithValue("dia_semana", !string.IsNullOrEmpty(horarioFuncionario.dia_semana) ? horarioFuncionario.dia_semana : horarioFuncionarioAtual.dia_semana);

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
        /// Remoção de um horário (diário) de um funcionário da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID do horário (diário) de um funcionário a ser removido</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Horario_Funcionario 
                            where id_funcionario_horario = @id_funcionario_horario";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_funcionario_horario", targetID);
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
        /// Leitura dos dados de todos os horários de um funcionário através do seu id de funcionário na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do funcionário ao qual pertencem os horarios a ser lido</param>
        /// <returns>Horarios do funcionário se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<HorarioFuncionario>> GetAllByFuncionarioIDService(string sqlDataSource, int targetID)

        {
            string query = @"select * from dbo.Horario_Funcionario where id_funcionario = @id_funcionario";

            try
            {
                List<HorarioFuncionario> horarioFuncionario = new List<HorarioFuncionario>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_funcionario", targetID);
                        myCommand.Parameters.AddWithValue("targetID", targetID);
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            HorarioFuncionario dia = new HorarioFuncionario();

                            dia.id_funcionario_horario = Convert.ToInt32(dataReader["id_funcionario_horario"]);
                            dia.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                            dia.hora_entrada = (TimeSpan)dataReader["hora_entrada"];
                            dia.hora_saida = (TimeSpan)dataReader["hora_saida"];
                            dia.dia_semana = dataReader["dia_semana"].ToString();

                            horarioFuncionario.Add(dia);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return horarioFuncionario;
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