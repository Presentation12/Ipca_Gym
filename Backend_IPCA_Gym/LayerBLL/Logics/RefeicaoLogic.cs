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
    public class RefeicaoLogic
    {
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Refeicao> refeicaoList = await RefeicaoService.GetAllService(sqlDataSource);

            if (refeicaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de refeicoes obtida com sucesso";
                response.Data = new JsonResult(refeicaoList);
            }

            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Refeicao refeicao = await RefeicaoService.GetByIDService(sqlDataSource, targetID);

            if (refeicao != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Refeicao obtida com sucesso!";
                response.Data = new JsonResult(refeicao);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, Refeicao newRefeicao)
        {
            Response response = new Response();
            bool creationResult = await RefeicaoService.PostService(sqlDataSource, newRefeicao);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Refeicao criada com sucesso!");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, Refeicao refeicao, int targetID)
        {
            Response response = new Response();
            bool updateResult = await RefeicaoService.PatchService(sqlDataSource, refeicao, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Refeicao alterada com sucesso!");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await RefeicaoService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Refeicao apagada com sucesso!");
            }

            return response;
        }
    }
}