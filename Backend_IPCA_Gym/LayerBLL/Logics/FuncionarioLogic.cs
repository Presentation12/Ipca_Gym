using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;
using Microsoft.IdentityModel.Tokens;
using Microsoft.Extensions.Configuration;

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade Funcionario
    /// </summary>
    public class FuncionarioLogic
    {
        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os funcionarios
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Funcionario> funcionarioList = await FuncionarioService.GetAllService(sqlDataSource);

            if (funcionarioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de funcionarios obtida com sucesso";
                response.Data = new JsonResult(funcionarioList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter um funcionario em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Funcionario que é pretendido retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Funcionario funcionario = await FuncionarioService.GetByIDService(sqlDataSource, targetID);

            if (funcionario != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Funcionario obtido com sucesso";
                response.Data = new JsonResult(funcionario);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar um funcionario
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newFuncionario">Objeto com os dados do Funcionario a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Funcionario newFuncionario)
        {
            Response response = new Response();
            bool creationResult = await FuncionarioService.PostService(sqlDataSource, newFuncionario);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Funcionario adicionado com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar um funcionario
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="funcionario">Objeto que contém os dados atualizados do Funcionario</param>
        /// <param name="targetID">ID do Funcionario que é pretendido atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Funcionario funcionario, int targetID)
        {
            Response response = new Response();
            bool updateResult = await FuncionarioService.PatchService(sqlDataSource, funcionario, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Funcionario alterado com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar um funcionario
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Funcionario que é pretendido eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await FuncionarioService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Funcionario removido com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de efetuar o login
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="conta">Model de login de Funcionario</param>
        /// <param name="_configuration">Dependency Injection</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> Login(string sqlDataSource, LoginFuncionario conta, IConfiguration _configuration)
        {
            Response response = new Response();
            string token = await FuncionarioService.LoginService(sqlDataSource, conta, _configuration);

            if (token.Length != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Login feito com sucesso!";
                response.Data = new JsonResult(token);
            }

            return response;
        }
    }
}
