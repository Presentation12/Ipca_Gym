using Backend_IPCA_Gym.Models;
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
    public class LojaController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public LojaController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Loja";
            List<Loja> produtos = new List<Loja>();

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

            return new JsonResult(produtos);
        }
    }
}
