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

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade Exercicio
    /// </summary>
    public class ExercicioLogic
    {
        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os exercicios
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Exercicio> exercicioList = await ExercicioService.GetAllService(sqlDataSource);

            if (exercicioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de exercicios obtido com sucesso";
                response.Data = new JsonResult(exercicioList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter um exercicio em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Exercicio que é pretendido retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Exercicio exercicio = await ExercicioService.GetByIDService(sqlDataSource, targetID);

            if (exercicio != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Exercicio obtido com sucesso";
                response.Data = new JsonResult(exercicio);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar um exercicio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newExercicio">Objeto com os dados do Exercicio a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Exercicio newExercicio)
        {
            Response response = new Response();
            bool creationResult = await ExercicioService.PostService(sqlDataSource, newExercicio);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Exercicio adicionado com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar um exercicio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="exercicio">Objeto que contém os dados atualizados do Exercicio</param>
        /// <param name="targetID">ID do Exercicio que é pretendido atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Exercicio exercicio, int targetID)
        {
            Response response = new Response();
            bool updateResult = await ExercicioService.PatchService(sqlDataSource, exercicio, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Exercicio alterado com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar um exercicio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Exercicio que é pretendido eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await ExercicioService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Exercicio removido com sucesso");
            }

            return response;
        }
    }
}
