﻿using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Npgsql;
using System.Data;
using System.Data.SqlClient;
using System.Numerics;
using System.Runtime.Intrinsics.Arm;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class HorarioFuncionarioController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public HorarioFuncionarioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Horario_Funcionario";
            List<HorarioFuncionario> horario = new List<HorarioFuncionario>();

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

                        horario.Add(dia);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(horario);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Horario_Funcionario where id_funcionario_horario = @id_funcionario_horario";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

                        return new JsonResult(targetHorarioFuncionario);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(HorarioFuncionario newHorarioFuncionario)
        {
            string query = @"
                            insert into dbo.Horario_Funcionario (id_funcionario, hora_entrada, hora_saida, dia_semana)
                            values (@id_funcionario, @hora_entrada, @hora_saida, @dia_semana)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

            return new JsonResult("Horário (dia) adicionado com sucesso");
        }

        [HttpPatch]
        public IActionResult Update(HorarioFuncionario exercicio)
        {
            string query = @"
                            update dbo.Horario_Funcionario 
                            set id_funcionario = @id_funcionario, 
                            hora_entrada = @hora_entrada, 
                            hora_saida = @hora_saida,
                            dia_semana = @dia_semana
                            where id_funcionario_horario = @id_funcionario_horario";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_funcionario_horario", exercicio.id_funcionario_horario);
                    myCommand.Parameters.AddWithValue("id_funcionario", exercicio.id_funcionario);
                    myCommand.Parameters.AddWithValue("hora_entrada", exercicio.hora_entrada);
                    myCommand.Parameters.AddWithValue("hora_saida", exercicio.hora_saida);
                    myCommand.Parameters.AddWithValue("dia_semana", exercicio.dia_semana);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Horário (dia) atualizado com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Horario_Funcionario 
                            where id_funcionario_horario = @id_funcionario_horario";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

            return new JsonResult("Horario (dia) apagado com sucesso");
        }
    }
}
