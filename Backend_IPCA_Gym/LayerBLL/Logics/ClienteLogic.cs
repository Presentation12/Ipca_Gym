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
using System.Net.Mail;
using System.Net;


namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade Cliente
    /// </summary>
    public class ClienteLogic
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os clientes
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Cliente> clienteList = await ClienteService.GetAllService(sqlDataSource);

            if (clienteList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de clientes obtidos com sucesso";
                response.Data = new JsonResult(clienteList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter um cliente em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Cliente que é pretendido retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Cliente cliente = await ClienteService.GetByIDService(sqlDataSource, targetID);

            if (cliente != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Cliente obtido com sucesso";
                response.Data = new JsonResult(cliente);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar um cliente
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newCliente">Objeto com os dados do Cliente a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Cliente newCliente)
        {
            Response response = new Response();
            bool creationResult = await ClienteService.PostService(sqlDataSource, newCliente);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Cliente adicionado com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar um cliente
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="cliente">Objeto que contém os dados atualizados do Cliente</param>
        /// <param name="targetID">ID do Cliente que é pretendido atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Cliente cliente, int targetID)
        {
            Response response = new Response();
            bool updateResult = await ClienteService.PatchService(sqlDataSource, cliente, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Cliente alterado com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar um cliente
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Cliente que é pretendido eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await ClienteService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Cliente removido com sucesso");
            }

            return response;
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Envia por email a password do utilizador quando este a deseja recuperar
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="clientId">ID do Cliente que pretende recuperar a password</param>
        /// <returns></returns>
        /// <exception cref="Exception"></exception>
        public async Task SendPasswordByEmail(string sqlDataSource, int clientId)
        {
            Cliente cliente = await ClienteService.GetByIDService(sqlDataSource, clientId);
            
            /*
            var password = "";

            // Crie um novo objeto HttpClient e faça a requisição para obter a senha do cliente
            using (var httpClient = new HttpClient())
            {
                var response = await httpClient.GetAsync($"https://example.com/api/clients/{clientId}/pass_salt");

                // Verifique se a requisição foi bem-sucedida
                if (!response.IsSuccessStatusCode)
                {
                    // A requisição falhou, então você pode lançar uma exceção ou retornar um erro
                    throw new Exception("Falha ao obter a senha do cliente");
                }

                // Obtenha a senha do cliente a partir da resposta da requisição
                password = await response.Content.ReadAsStringAsync();
            }
            */

            // Crie um novo objeto SmtpClient para enviar o email
            using (var smtpClient = new SmtpClient())
            {
                // Configure as configurações de servidor SMTP e autenticação
                smtpClient.Host = "smtp.gmail.com";
                smtpClient.Port = 587;
                smtpClient.UseDefaultCredentials = false;
                smtpClient.Credentials = new NetworkCredential("feedycorps@gmail.com", "melhorprojeto2022");
                smtpClient.EnableSsl = true;

                // Crie o email a ser enviado
                var mailMessage = new MailMessage();
                mailMessage.From = new MailAddress("feedycorps@gmail.com");
                mailMessage.To.Add("joaocarlosapresentacao@gmail.com");
                mailMessage.Subject = "Sua senha";
                mailMessage.Body = $"Sua senha é: {cliente.pass_salt}";

                // Envie o email
                await smtpClient.SendMailAsync(mailMessage);
            }

            
        }
        #endregion
    }
}
