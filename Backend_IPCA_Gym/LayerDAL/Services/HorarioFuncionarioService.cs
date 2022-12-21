using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class HorarioFuncionarioService
    {
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
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<HorarioFuncionario> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Horario_Funcionario where id_horario_funcionario = @id_horario_funcionario";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_horario_funcionario", targetID);

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
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

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
            catch (Exception ex)
            {
                Console.Write(ex.ToString());
                return false;
            }
        }

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
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

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
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }
    }
}