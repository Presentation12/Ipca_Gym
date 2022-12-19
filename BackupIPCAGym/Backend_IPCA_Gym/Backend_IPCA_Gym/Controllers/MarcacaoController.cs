﻿using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MarcacaoController : ControllerBase
    {

        private readonly IConfiguration _configuration;
        public MarcacaoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        protected Marcacao GetMarcacaoByID(int targetID)
        {
            string query = @"select * from dbo.Marcacao where id_marcacao = @id_marcacao";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Marcacao";
            List<Marcacao> marcacoes = new List<Marcacao>();

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

            return new JsonResult(marcacoes);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Marcacao where id_marcacao = @id_marcacao";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

                        return new JsonResult(targetMarcacao);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Marcacao newMarcacao)
        {
            string query = @"
                            insert into dbo.Marcacao (id_funcionario, id_cliente, data_marcacao, descricao, estado)
                            values (@id_funcionario, @id_cliente, @data_marcacao, @descricao, @estado)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

            return new JsonResult("Marcação adicionada com sucesso");
        }

        [HttpPatch("{targetID}")]
        public IActionResult Update(Marcacao marcacao, int targetID)
        {
            string query = @"
                            update dbo.Marcacao 
                            set id_funcionario = @id_funcionario, 
                            id_cliente = @id_cliente, 
                            data_marcacao = @data_marcacao,
                            descricao = @descricao,
                            estado = @estado
                            where id_marcacao = @id_marcacao";

            Marcacao marcacaoAtual = GetMarcacaoByID(targetID);

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    if (marcacao.id_marcacao != null) myCommand.Parameters.AddWithValue("id_marcacao", marcacao.id_marcacao);
                    else myCommand.Parameters.AddWithValue("id_marcacao", marcacaoAtual.id_marcacao);

                    if (marcacao.id_funcionario != null) myCommand.Parameters.AddWithValue("id_funcionario", marcacao.id_funcionario);
                    else myCommand.Parameters.AddWithValue("id_funcionario", marcacaoAtual.id_funcionario);

                    if (marcacao.id_cliente != null) myCommand.Parameters.AddWithValue("id_cliente", marcacao.id_cliente);
                    else myCommand.Parameters.AddWithValue("id_cliente", marcacaoAtual.id_cliente);

                    if (marcacao.data_marcacao != null) myCommand.Parameters.AddWithValue("data_marcacao", Convert.ToDateTime(marcacao.data_marcacao));
                    else myCommand.Parameters.AddWithValue("data_marcacao", Convert.ToDateTime(marcacaoAtual.data_marcacao));

                    if (!string.IsNullOrEmpty(marcacao.descricao)) myCommand.Parameters.AddWithValue("descricao", marcacao.descricao);
                    else myCommand.Parameters.AddWithValue("descricao", marcacaoAtual.descricao);

                    if (!string.IsNullOrEmpty(marcacao.estado)) myCommand.Parameters.AddWithValue("estado", marcacao.estado);
                    else myCommand.Parameters.AddWithValue("estado", marcacaoAtual.estado);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Marcação atualizada com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Marcacao 
                            where id_marcacao = @id_marcacao";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

            return new JsonResult("Marcação apagada com sucesso");
        }
    }
}