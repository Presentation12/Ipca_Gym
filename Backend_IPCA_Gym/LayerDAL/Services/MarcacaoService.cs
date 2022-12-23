using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class MarcacaoService
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Leitura dos dados de todas as marcações da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de marcações se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Marcacao>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Marcacao";

            try
            {
                List<Marcacao> marcacoes = new List<Marcacao>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Marcacao marcacao = new Marcacao();

                            marcacao.id_marcacao = Convert.ToInt32(dataReader["id_marcacao"]);
                            marcacao.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                            marcacao.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                            marcacao.data_marcacao = Convert.ToDateTime(dataReader["data_marcacao"]);
                            marcacao.descricao = dataReader["descricao"].ToString();
                            marcacao.estado = dataReader["estado"].ToString();
                            marcacoes.Add(marcacao);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return marcacoes;
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
                Console.WriteLine(ex.ToString());
                return null;
            }
        }

        /// <summary>
        /// Leitura dos dados de uma marcação através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID da marcação a ser lida</param>
        /// <returns>Marcação se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Marcacao> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Marcacao where id_marcacao = @id_marcacao";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_marcacao", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Marcacao targetMarcacao = new Marcacao();
                            targetMarcacao.id_marcacao = reader.GetInt32(0);
                            targetMarcacao.id_funcionario = reader.GetInt32(1);
                            targetMarcacao.id_cliente = reader.GetInt32(2);
                            targetMarcacao.data_marcacao = reader.GetDateTime(3);
                            targetMarcacao.descricao = reader.GetString(4);
                            targetMarcacao.estado = reader.GetString(5);

                            reader.Close();
                            databaseConnection.Close();

                            return targetMarcacao;
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
        /// Inserção dos dados de uma nova marcação na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newMarcacao">Objeto com os dados da nova marcação</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PostService(string sqlDataSource, Marcacao newMarcacao)
        {
            string query = @"
                            insert into dbo.Marcacao (id_funcionario, id_cliente, data_marcacao, descricao, estado)
                            values (@id_funcionario, @id_cliente, @data_marcacao, @descricao, @estado)";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_funcionario", newMarcacao.id_funcionario);
                        myCommand.Parameters.AddWithValue("id_cliente", newMarcacao.id_cliente);
                        myCommand.Parameters.AddWithValue("data_marcacao", Convert.ToDateTime(newMarcacao.data_marcacao));
                        myCommand.Parameters.AddWithValue("descricao", newMarcacao.descricao);
                        myCommand.Parameters.AddWithValue("estado", newMarcacao.estado);

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
        /// Atualiza os dados de uma marcação através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="marcação">Objeto com os novos dados da marcação</param>
        /// <param name="targetID">ID da marcação a ser atualizada</param>
        /// <returns>True se a atualização dos dados foi bem sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PatchService(string sqlDataSource, Marcacao marcacao, int targetID)
        {
            string query = @"
                            update dbo.Marcacao 
                            set id_funcionario = @id_funcionario, 
                            id_cliente = @id_cliente, 
                            data_marcacao = @data_marcacao,
                            descricao = @descricao,
                            estado = @estado
                            where id_marcacao = @id_marcacao";

            try
            {
                Marcacao marcacaoAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_marcacao", marcacao.id_marcacao != 0 ? marcacao.id_marcacao : marcacaoAtual.id_marcacao);
                        myCommand.Parameters.AddWithValue("id_funcionario", marcacao.id_funcionario != 0 ? marcacao.id_funcionario : marcacaoAtual.id_funcionario);
                        myCommand.Parameters.AddWithValue("id_cliente", marcacao.id_cliente != 0 ? marcacao.id_cliente : marcacaoAtual.id_cliente);
                        myCommand.Parameters.AddWithValue("data_marcacao", !marcacao.data_marcacao.Equals(DateTime.MinValue) ? Convert.ToDateTime(marcacao.data_marcacao) : Convert.ToDateTime(marcacaoAtual.data_marcacao));
                        myCommand.Parameters.AddWithValue("descricao", !string.IsNullOrEmpty(marcacao.descricao) ? marcacao.descricao : marcacaoAtual.descricao);
                        myCommand.Parameters.AddWithValue("estado", !string.IsNullOrEmpty(marcacao.estado) ? marcacao.estado : marcacaoAtual.estado);

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
        /// Remoção de uma marcação da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID da marcação a ser removida</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Marcacao 
                            where id_marcacao = @id_marcacao";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_marcacao", targetID);
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
        /// Leitura dos dados de todos as marcações de um funcionário através do seu id de funcionário na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do funcionário ao qual pertencem as marcações a ser lido</param>
        /// <returns>Marcações do funcionário se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Marcacao>> GetAllByFuncionarioIDService(string sqlDataSource, int targetID)

        {
            string query = @"select * from dbo.Marcacao where id_funcionario = @id_funcionario";

            try
            {
                List<Marcacao> marcacoes = new List<Marcacao>();
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
                            Marcacao marcacao = new Marcacao();

                            marcacao.id_marcacao = Convert.ToInt32(dataReader["id_marcacao"]);
                            marcacao.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                            marcacao.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                            marcacao.data_marcacao = Convert.ToDateTime(dataReader["data_marcacao"]);
                            marcacao.descricao = dataReader["descricao"].ToString();
                            marcacao.estado = dataReader["estado"].ToString();
                            marcacoes.Add(marcacao);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return marcacoes;
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
        /// Inserção dos dados de uma nova marcação na base de dados com o uso das regras de negócio:
        /// -> Verifica se o funcionário tem horários
        /// -> Verifica se o funcionário trabalha naquele horário
        /// -> Verifica se o funcionário não tem uma marcação para essa hora nesse dia
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newMarcacao">Objeto com os dados da nova marcação</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PostCheckedService(string sqlDataSource, Marcacao newMarcacao)
        {
            string query = @"
                            insert into dbo.Marcacao (id_funcionario, id_cliente, data_marcacao, descricao, estado)
                            values (@id_funcionario, @id_cliente, @data_marcacao, @descricao, @estado)";

            try
            {
                // Buscar horario completo do funcionario pedido
                List<HorarioFuncionario> horarioFuncionario = await HorarioFuncionarioService.GetAllByFuncionarioIDService(sqlDataSource, newMarcacao.id_funcionario);

                // Verifica se o funcionário tem horários
                if (horarioFuncionario.Count == 0) return false;

                // Verifica se o funcionário trabalha naquele horário
                int count = 0;
                foreach (HorarioFuncionario dia in horarioFuncionario)
                {
                    // comparar horario do dia com a data marcada -> veririficar se são o mesmo dia da semaana e se esta dentro do intervalo de horarios
                    if (newMarcacao.data_marcacao.DayOfWeek.ToString() == dia.dia_semana && newMarcacao.data_marcacao.TimeOfDay >= dia.hora_entrada && newMarcacao.data_marcacao.TimeOfDay <= dia.hora_saida)
                    {
                        // horario compativel fase 1
                        count = 1;
                    }
                }
                // horario imcompativel com este funcionario
                if(count == 0) return false;

                // Buscar lista de marcações do funcionario
                List<Marcacao> marcacoesFuncionario = await MarcacaoService.GetAllByFuncionarioIDService(sqlDataSource, newMarcacao.id_funcionario);
                // Verifica se o funcionário não tem uma marcação para essa hora nesse dia
                foreach (Marcacao marcacaoFuncionario in marcacoesFuncionario)
                {
                    // compara a data e verifica se 
                    if (marcacaoFuncionario.data_marcacao.Date == newMarcacao.data_marcacao.Date && newMarcacao.data_marcacao.TimeOfDay >= newMarcacao.data_marcacao.TimeOfDay && newMarcacao.data_marcacao.TimeOfDay <= (newMarcacao.data_marcacao.TimeOfDay + TimeSpan.FromHours(2)))
                    {
                        // horario imcompativel por que funcionario ja tem marcação
                        return false;
                    }
                }

                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_funcionario", newMarcacao.id_funcionario);
                        myCommand.Parameters.AddWithValue("id_cliente", newMarcacao.id_cliente);
                        myCommand.Parameters.AddWithValue("data_marcacao", Convert.ToDateTime(newMarcacao.data_marcacao));
                        myCommand.Parameters.AddWithValue("descricao", newMarcacao.descricao);
                        myCommand.Parameters.AddWithValue("estado", newMarcacao.estado);

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

        #endregion
    }
}

