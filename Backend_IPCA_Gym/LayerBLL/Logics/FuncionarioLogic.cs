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
        /// Método que recebe os dados do serviço de obter todos os funcionarios ativos
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllAtivoLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Funcionario> funcionarioList = await FuncionarioService.GetAllAtivoService(sqlDataSource);

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
        public static async Task<Response> LoginLogic(string sqlDataSource, LoginFuncionario conta, IConfiguration _configuration)
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

        /// <summary>
        /// Método que recebe a resposta do serviço de adicionar um cliente por parte de um funcionario
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newCliente">Objeto que contém os dados do novo cliente</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> RegistClientLogic(string sqlDataSource, Cliente newCliente)
        {
            Response response = new Response();
            bool creationResult = await FuncionarioService.RegistClienteService(sqlDataSource, newCliente);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Cliente adicionado com sucesso!");
            }

            return response;
        }



        /// <summary>
        /// Método que recebe a resposta do serviço de recuperar uma password de um funcionario
        /// </summary>
        /// <param name="codigo">Codigo do funcionario que se pretende recuperar a password</param>
        /// <param name="password">Nova password</param>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> RecoverPasswordLogic(int codigo, string password, string sqlDataSource)
        {
            Response response = new Response();
            bool recoverResult = await FuncionarioService.RecoverPasswordService(codigo, password, sqlDataSource);

            if (recoverResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Password alterada com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de editar um cliente
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do cliente que se pretende alterar</param>
        /// <param name="cliente">Objeto que contêm os dados atualizados do cliente</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> EditClienteLogic(string sqlDataSource, int targetID, Cliente cliente)
        {
            Response response = new Response();
            bool editResult = await FuncionarioService.EditClienteService(sqlDataSource, targetID, cliente);

            if (editResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Cliente editado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de alterar stock de um produto
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="quantidade">Novo valor de quantidade de stock na loja</param>
        /// <param name="targetID">ID do produto da Loja que se pretende alterar stock</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> EditLojaStockLogic(string sqlDataSource, int quantidade, int targetID)
        {
            Response response = new Response();
            bool editResult = await FuncionarioService.EditLojaStockService(sqlDataSource, quantidade, targetID);

            if (editResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Stock do produto alterado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de modificar um produto
        /// </summary>
        /// <param name="sqlDataSource">String de conexão à base de dados</param>
        /// <param name="produto">Objeto que contém dados atualizados do produto da Loja</param>
        /// <param name="targetID">ID do produto pretendido a mudar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> EditLojaLogic(string sqlDataSource, Loja produto, int targetID)
        {
            Response response = new Response();
            bool editResult = await FuncionarioService.EditLojaService(sqlDataSource, produto, targetID);

            if (editResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto modificado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de alterar a lotacao atual do ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de conexão à base de dados</param>
        /// <param name="targetID">ID do ginásio a alterar a sua ocupação</param>
        /// <param name="lotacao">Valor da lotação atual do ginásio</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> EditLotacaoGymLogic(string sqlDataSource, int targetID, int lotacao)
        {
            Response response = new Response();
            bool editResult = await FuncionarioService.EditLotacaoGymLogicService(sqlDataSource, targetID, lotacao);

            if (editResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Lotacao do ginasio alterada com sucesso!");
            }


            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de ver avaliaçoes de um ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de conexão à base de dados</param>
        /// <param name="codigofuncionario">Codigo do funcionario que faz o request</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAvaliacoesOnGymLogic(string sqlDataSource, int codigofuncionario) 
        {
            Response response = new Response();
            List<Classificacao> list = await FuncionarioService.GetAvaliacoesOnGymService(sqlDataSource, codigofuncionario);

            if (list != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = $"Lista de avaliacoes do ginasio obtida com sucesso";
                response.Data = new JsonResult(list);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de remover funcionario do ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de conexão à base de dados</param>
        /// <param name="targetID">ID do funcionário</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteFuncionarioLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool editResult = await FuncionarioService.DeleteFuncionarioService(sqlDataSource, targetID);

            if (editResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Funcionário removido com sucesso!");
            }


            return response;
        }
    }
}
